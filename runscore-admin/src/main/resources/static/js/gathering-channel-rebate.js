var gatheringChannelRebateVM = new Vue({
	el : '#gathering-channel-rebate',
	data : {
		channelDictItems : [],
		selectedChannel : {},
		rebates : [],

		quickSettingFlag : false,
		lowestRebate : null,
		highestRebate : null,
		rebateStep : null,
		quickSettingRebates : [],

		newRebate : '',
		addRebateFlag : false
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadChannelDictItem();
		this.loadGatheringChannelRebate();
	},
	methods : {

		loadChannelDictItem : function() {
			var that = this;
			that.$http.get('/gatheringChannel/findAllGatheringChannel', {}).then(function(res) {
				that.channelDictItems = res.body.data;
				if (that.channelDictItems.length > 0) {
					that.selectedChannel = that.channelDictItems[0];
				} else {
					that.selectedChannel = {};
				}
			});
		},

		loadGatheringChannelRebate : function() {
			var that = this;
			that.$http.get('/gatheringChannel/findAllGatheringChannelRebate', {}).then(function(res) {
				that.rebates = res.body.data;
			});
		},

		showQuickSettingModal : function() {
			this.quickSettingFlag = true;
			this.lowestRebate = null;
			this.highestRebate = null;
			this.rebateStep = null;
			this.quickSettingRebates = [];
		},

		addRecord : function() {
			this.quickSettingRebates.push(null);
		},

		generateRecord : function() {
			if (this.lowestRebate == null || this.lowestRebate === '') {
				layer.alert('请输入最低返点', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.highestRebate == null || this.highestRebate === '') {
				layer.alert('请输入最高返点', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.rebateStep == null || this.rebateStep === '') {
				layer.alert('请输入返点步长', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (this.lowestRebate >= this.highestRebate) {
				layer.alert('最低返点必须要小于最高返点', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var quickSettingRebates = [];
			for (var i = this.lowestRebate; i <= this.highestRebate; i = numberFormat(i + this.rebateStep)) {
				quickSettingRebates.push(numberFormat(i));
			}
			if (quickSettingRebates.length == 0) {
				layer.alert('返点生成的记录长度为0,请重新调整', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			this.quickSettingRebates = quickSettingRebates;
		},

		saveRebate : function() {
			var that = this;
			if (this.quickSettingRebates.length == 0) {
				layer.alert('请增加返点记录', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var map = new Map();
			for (var i = 0; i < this.quickSettingRebates.length; i++) {
				var rebate = this.quickSettingRebates[i];
				if (rebate === null || rebate === '') {
					layer.alert('请输入返点', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				var key = rebate;
				if (map.get(key)) {
					layer.alert('不能设置重复的返点', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				map.set(key, key);
			}
			that.$http.post('/gatheringChannel/resetRebate', {
				channelId : that.selectedChannel.id,
				rebates : that.quickSettingRebates
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.quickSettingFlag = false;
				that.loadGatheringChannelRebate();
			});
		},

		showAddRebateModal : function() {
			this.newRebate = '';
			this.addRebateFlag = true;
		},

		addRebate : function() {
			var that = this;
			if (that.newRebate === null || that.newRebate === '') {
				layer.alert('请输入返点', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/gatheringChannel/addRebate', {
				channelId : that.selectedChannel.id,
				rebate : that.newRebate
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addRebateFlag = false;
				that.loadGatheringChannelRebate();
			});
		},

		delRebate : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/gatheringChannel/delRebate', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.loadGatheringChannelRebate();
				});
			});
		}

	}
});