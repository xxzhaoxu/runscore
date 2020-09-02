var channelAnalysisVM = new Vue({
	el : '#channel-analysis',
	data : {
		global : GLOBAL,
		totalStatistical : {},
		monthStatistical : {},
		todayStatistical : {},
		everydayStatisticals : []
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.loadTotalStatistical();
		that.loadMonthStatistical();
		that.loadTodayStatistical();
	},
	methods : {

		loadTotalStatistical : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findTotalStatistical').then(function(res) {
				that.totalStatistical = res.body.data;
			});
		},

		loadMonthStatistical : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findMonthStatistical').then(function(res) {
				that.monthStatistical = res.body.data;
			});
		},

		loadTodayStatistical : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findTodayStatistical').then(function(res) {
				that.todayStatistical = res.body.data;
			});
		}

	}
});