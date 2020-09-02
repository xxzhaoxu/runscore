var totalBountyRankChart = null;
var todayBountyRankChart = null;
var statisticalAnalysisVM = new Vue({
	el : '#agent-performance',
	data : {
		global : GLOBAL,
		merchantTradeResultsSituations : [],
		queryRScope : 'today',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.loadMerchantTradeResultsSituation();
	},
	methods : {

		loadMerchantTradeResultsSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findMerchantTradeResultsSituation').then(function(res) {
				that.merchantTradeResultsSituations = res.body.data;
			});
		}
	}
});