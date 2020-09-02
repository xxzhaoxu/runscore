var rateDetailsVM = new Vue({
	el : '#rate-details',
	data : {
		global : GLOBAL,
		channelRates : []
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadChannelRate();
	},
	methods : {

		loadChannelRate : function() {
			var that = this;
			that.$http.get('/gatheringChannel/findGatheringChannelRateByMerchantId').then(function(res) {
				this.channelRates = res.body.data;
			});
		}
	}
});