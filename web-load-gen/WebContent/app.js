var stat = {stopped: true};
var requestMode = null;
var chart = null;
var lastTime = 0;

$(document).ready(function() {
	$('#start').click(function(e) {
		requestMode = 'start';
		start();
		updateButtons();
		e.preventDefault();
		return false;
	});
	
	$('#stop').click(function(e) {
		requestMode = 'stop';
		stop();
		updateButtons();
		e.preventDefault();
		return false;
	});

	function durationFormatter(defaultValue) {
		return function (value) {
			var allSeconds = parseInt(value, 10);
	        if(allSeconds == 0 || isNaN(allSeconds)) return defaultValue || '';
	    	var seconds = allSeconds % 60;
	    	var allMinutes = Math.floor(allSeconds / 60);
	    	var minutes = allMinutes % 60;
	    	var hours = Math.floor(allMinutes / 60);
	    	return (hours > 0 ? hours + 'h ' : '') +
	    			(minutes > 0 ? minutes + 'm ': '') +
	    			seconds + 's';
		};
	}
	
	function resetChart() {
		var points = [];
		for(var i = 0; i < 100; i++) {
			points.push({seconds: 0});
		}
		chart.dataProvider = points;
		
		var axis = chart.getValueAxisById('ops');
		var guide = axis.guides[0];
		axis.removeGuide(guide);
		var load = $('#load').val();
		guide.value = load;
		axis.addGuide(guide);
	}
	
	var opsAxis = new AmCharts.ValueAxis();
	opsAxis.id = 'ops';
	$.extend(opsAxis, {
    	id: 'ops',
        axisAlpha: 0,
        inside: true,
        position: 'left',
        gridThickness: 0,
        guides: [{value: 100, lineThickness: 1, dashLength: 10}],
        minimum: 0,
        title: 'ops/sec',
        minMaxMultiplier: 1.3
	});
	
	chart = AmCharts.makeChart('chart', {
	    type: 'serial',
	    valueAxes: [
			opsAxis, {
		    	id: 'latency',
		        axisAlpha: 0,
		        inside: false,
		        position: 'right',
		        gridThickness: 0,
		        title: 'Latency'
		    }
		],
	    graphs: [
	        {
	        	title: 'Operations per second',
		        lineColor: '#d1655d',
		        lineThickness: 2,
		        negativeLineColor: '#637bb6',
		        type: 'smoothedLine',
		        valueField: 'opsPerSecond',
		        valueAxis: 'ops',
		        balloonText: "[[title]]<br><b><span style='font-size:14px;'>[[value]]</span></b>"
		    }, {
		    	title: 'Average Latency (updates)',
		        lineThickness: 2,
		    	valueField: 'updateAvgLatency',
		    	type: 'smoothedLine',
		    	valueAxis: 'latency',
		        balloonText: "[[title]]<br><b><span style='font-size:14px;'>[[value]]</span></b>"
		    }, {
		    	title: 'Average Latency (reads)',
		        lineThickness: 2,
		    	valueField: 'readAvgLatency',
		    	type: 'smoothedLine',
		    	valueAxis: 'latency',
		        balloonText: "[[title]]<br><b><span style='font-size:14px;'>[[value]]</span></b>"
		    }
		],
	    chartCursor: {
	        cursorAlpha: 0,
	        cursorPosition: 'mouse',
	        bulletsEnabled: true,
	        selectWithoutZooming: true,
	        zoomable: false,
	        categoryBalloonFunction: durationFormatter('No Data')
	    },
	    categoryField: 'seconds',
	    categoryAxis: {
	    	autoGridCount: true,
	    	minHorizontalGap: 100,
	        labelFunction: durationFormatter()
	    },
	    numberFormatter: {
	    	precision: 2
	    },
	    legend: {
	    	position: 'bottom'
	    }
	});
	resetChart();

	function updateButtons() {
		var startEnabled = !!stat.stopped && !requestMode;
		$('#start').prop('disabled', !startEnabled);
		var stopEnabled = !stat.stopped && !requestMode;
		$('#stop').prop('disabled', !stopEnabled);
	}

	function onStatusReceive(s) {
		if(requestMode == 'start' && !stat.stopped) {
			requestMode = null;
		}
		if(requestMode == 'stop' && stat.stopped) {
			requestMode = null;
		}
		stat = s;
		updateButtons();
		updateStatusView();
	}

	function updateStatusView() {
//		var txt = '';
		if(!stat.stopped) {
			if(stat.seconds != lastTime) {
				if(chart.dataProvider.length == 100) {
					chart.dataProvider.unshift();
				}
				chart.dataProvider.push(stat);
				chart.validateData();
				lastTime = stat.seconds;
			}
//			txt = 'ops: ' + stat.ops + ', ops/sec: ' + stat.opsPerSecond + 
//			', update avg latency: ' + stat.updateAvgLatency + 
//			', read avg latency: ' + stat.readAvgLatency;
		}
//		$('#status').text(txt);
		
	}

	function start() {
		resetChart();
		$.post('launcher/start?' + $('#info').serialize());
	}

	function stop() {
		$.post('launcher/stop');
		
	}

	function listenUpdates() {
		$.get('launcher/status', onStatusReceive);
	}

	setInterval(listenUpdates, 1000);
});
