var realTimeQueueRecordVM = new Vue({
	el : '#real-time-queue-record',
	data : {
		global : GLOBAL,
		realTimeQueueRecords : [],
		realTimeQueueFlag : true,
	},
	computed : {},
	created : function() {
		var that = this;
		that.getRealTimeQueueRecord();
		window.setInterval(function() {
			that.getRealTimeQueueRecord();
		}, 6000);
	},
	mounted : function() {
	},
	methods : {
		getRealTimeQueueRecord : function() {
			var that = this;
			layer.msg('获取实时排队记录...', {
				icon : 16,
				shade : 0.01,
				time : 3500
			});
			that.realTimeQueueRecords = [];
			that.$http.get('/merchantOrder/realTimeQueueRecord').then(function(res) {
				that.realTimeQueueRecords = res.body.data;
				if(that.realTimeQueueRecords.length == 0){
					that.realTimeQueueFlag= true;
				}else{
					that.realTimeQueueFlag= false;
				}
				layer.closeAll();
			});
		},

		updateReceiveOrderStateToStop : function(userAccountId) {
			var that = this;
			that.$http.get('/userAccount/updateReceiveOrderStateToStop', {
				params : {
					userAccountId : userAccountId
				}
			}).then(function(res) {
				that.getRealTimeQueueRecord();
			});
		},

	}
});