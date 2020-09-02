var totalBountyRankChart = null;
var todayBountyRankChart = null;
var statisticalAnalysisVM = new Vue({
	el : '#statistical-analysis',
	data : {
		global : GLOBAL,
		cashDepositBounty : {},
		tradeSituation : {},
		rechargeSituation : {},
		withdrawSituation : {},
		adjustCashDepositSituation : {},
		channelTradeSituations : [],
		topAgentGatheringSituations : [],
		creditMemberGatheringSituations : [],
		merchantTradeSituations : [],
		merchantTradeResultsSituations : [],
		queryScope : 'today',
		queryRScope : 'today',
		channelStartTime : dayjs().format('YYYY-MM-DD'),
		channelEndTime : dayjs().format('YYYY-MM-DD'),
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		that.loadCashDepositBounty();
		that.loadTradeSituation();
		that.loadMerchantTradeSituation();

		that.loadRechargeSituation();
		that.loadWithdrawSituation();
		that.loadAdjustCashDepositSituation();
		that.loadChannelTradeSituation();
		//that.loadMerchantTradeResultsSituation();

		//that.initTotalBountyRankChart();
		//that.loadTotalTop10BountyRankData();

		//that.initTodayBountyRankChart();
		//that.loadTodayTop10BountyRankData();
	},
	methods : {

		toMerchantOrderPage : function(merchantName, orderState, queryScope) {
			var submitStartTime = 'all';
			var submitEndTime = 'all';
			if (queryScope == 'month') {
				submitStartTime = dayjs().startOf('month').format('YYYY-MM-DD');
				submitEndTime = dayjs().endOf('month').format('YYYY-MM-DD');
			} else if (queryScope == 'yesterday') {
				submitStartTime = dayjs().subtract(1, 'day').format('YYYY-MM-DD');
				submitEndTime = dayjs().subtract(1, 'day').format('YYYY-MM-DD');
			} else if (queryScope == 'today') {
				submitStartTime = dayjs().format('YYYY-MM-DD');
				submitEndTime = dayjs().format('YYYY-MM-DD');
			}
			window.location.href = 'merchant-order?merchantName=' + encodeURI(encodeURI(merchantName)) + '&orderState=' + orderState + '&submitStartTime=' + submitStartTime + '&submitEndTime=' + submitEndTime;
		},

		setChannelQueryCond : function(queryScope) {
			var channelStartTime = 'all';
			var channelEndTime = 'all';
			if (queryScope == 'all') {
				channelStartTime = null;
				channelEndTime = null;
			} else if (queryScope == 'month') {
				channelStartTime = dayjs().startOf('month').format('YYYY-MM-DD');
				channelEndTime = dayjs().endOf('month').format('YYYY-MM-DD');
			} else if (queryScope == 'yesterday') {
				channelStartTime = dayjs().subtract(1, 'day').format('YYYY-MM-DD');
				channelEndTime = dayjs().subtract(1, 'day').format('YYYY-MM-DD');
			} else if (queryScope == 'today') {
				channelStartTime = dayjs().format('YYYY-MM-DD');
				channelEndTime = dayjs().format('YYYY-MM-DD');
			}
			this.channelStartTime = channelStartTime;
			this.channelEndTime = channelEndTime;
			this.loadChannelTradeSituation();
		},

		loadMerchantTradeSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findMerchantTradeSituation').then(function(res) {
				that.merchantTradeSituations = res.body.data;
			});
		},

		loadMerchantTradeResultsSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findMerchantTradeResultsSituation').then(function(res) {
				that.merchantTradeResultsSituations = res.body.data;
			});
		},

		loadCashDepositBounty : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findCashDepositBounty').then(function(res) {
				that.cashDepositBounty = res.body.data;
			});
		},

		loadChannelTradeSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findChannelTradeSituation', {
				params : {
					startTime : that.channelStartTime,
					endTime : that.channelEndTime
				}
			}).then(function(res) {
				that.channelTradeSituations = res.body.data;
			});
		},

		loadRechargeSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findRechargeSituation').then(function(res) {
				that.rechargeSituation = res.body.data;
			});
		},

		loadWithdrawSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findWithdrawSituation').then(function(res) {
				that.withdrawSituation = res.body.data;
			});
		},

		loadAdjustCashDepositSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findAdjustCashDepositSituation').then(function(res) {
				that.adjustCashDepositSituation = res.body.data;
			});
		},

		loadTradeSituation : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findTradeSituation').then(function(res) {
				that.tradeSituation = res.body.data;
			});
		},

		initTotalBountyRankChart : function() {
			var that = this;
			option = {
				title : {
					text : '累计奖励金排行榜前十'
				},
				color : [ '#26dad0' ],
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow'
					}
				},
				grid : {
					show : true,
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true,
					backgroundColor : '#2c3448'
				},
				xAxis : {
					type : 'category',
					data : [],
					axisTick : {
						alignWithLabel : true
					}
				},
				yAxis : {
					type : 'value'
				},
				series : [ {
					name : '奖励金',
					type : 'bar',
					barWidth : '60%',
					data : []
				} ]
			};
			totalBountyRankChart = echarts.init(document.getElementById('total-bounty-rank-chart'));
			totalBountyRankChart.setOption(option);
		},

		initTodayBountyRankChart : function() {
			var that = this;
			option = {
				title : {
					text : '今日奖励金排行榜前十'
				},
				color : [ '#26dad0' ],
				tooltip : {
					trigger : 'axis',
					axisPointer : {
						type : 'shadow'
					}
				},
				grid : {
					show : true,
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true,
					backgroundColor : '#2c3448'
				},
				xAxis : {
					type : 'category',
					data : [],
					axisTick : {
						alignWithLabel : true
					}
				},
				yAxis : {
					type : 'value'
				},
				series : [ {
					name : '奖励金',
					type : 'bar',
					barWidth : '60%',
					data : []
				} ]
			};
			todayBountyRankChart = echarts.init(document.getElementById('today-bounty-rank-chart'));
			todayBountyRankChart.setOption(option);
		},

		loadTotalTop10BountyRankData : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findTotalTop10BountyRank').then(function(res) {
				var xAxisDatas = [];
				var seriesDatas = [];
				var top10BountyRanks = res.body.data;
				for (var i = 0; i < top10BountyRanks.length; i++) {
					xAxisDatas.push(top10BountyRanks[i].userName);
					seriesDatas.push(top10BountyRanks[i].bounty);
				}
				totalBountyRankChart.setOption({
					xAxis : {
						data : xAxisDatas
					},
					series : [ {
						data : seriesDatas
					} ]
				});
			});
		},

		loadTodayTop10BountyRankData : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findTodayTop10BountyRank').then(function(res) {
				var xAxisDatas = [];
				var seriesDatas = [];
				var top10BountyRanks = res.body.data;
				for (var i = 0; i < top10BountyRanks.length; i++) {
					xAxisDatas.push(top10BountyRanks[i].userName);
					seriesDatas.push(top10BountyRanks[i].bounty);
				}
				todayBountyRankChart.setOption({
					xAxis : {
						data : xAxisDatas
					},
					series : [ {
						data : seriesDatas
					} ]
				});
			});
		}
	}
});