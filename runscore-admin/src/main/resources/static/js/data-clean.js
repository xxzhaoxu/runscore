var dataCleanVM = new Vue({
	el : '#data-clean',
	data : {
		global : GLOBAL,
		dataTypes : [ {
			type : 'merchantOrder',
			name : '商户订单',
			selectedFlag : false
		}, {
			type : 'appeal',
			name : '申诉记录',
			selectedFlag : false
		}, {
			type : 'merchantSettlementRecord',
			name : '商户结算记录',
			selectedFlag : false
		}, {
			type : 'rechargeOrder',
			name : '充值订单',
			selectedFlag : false
		}, {
			type : 'withdrawRecord',
			name : '提现记录',
			selectedFlag : false
		}, {
			type : 'inviteCode',
			name : '邀请码',
			selectedFlag : false
		}, {
			type : 'gatheringCode',
			name : '收款码',
			selectedFlag : false
		}, {
			type : 'accountChangeLog',
			name : '帐变日志',
			selectedFlag : false
		}, {
			type : 'loginLog',
			name : '登录日志',
			selectedFlag : false
		}, {
			type : 'operLog',
			name : '操作日志',
			selectedFlag : false
		} ],
		startTime : '',
		endTime : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
	},
	methods : {

		dataClean : function() {
			var that = this;
			layer.confirm('确定要清理数据吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.dataCleanInner();
			});
		},

		dataCleanInner : function() {
			var that = this;
			var selectedDataTypes = [];
			for (var i = 0; i < that.dataTypes.length; i++) {
				var dataType = that.dataTypes[i];
				if (dataType.selectedFlag) {
					selectedDataTypes.push(dataType.type);
				}
			}
			if (selectedDataTypes.length == 0) {
				layer.alert('请选择数据类型', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.startTime === null || that.startTime === '') {
				layer.alert('请选择需要清理的时间范围', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.endTime === null || that.endTime === '') {
				layer.alert('请选择需要清理的时间范围', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/dataClean/dataClean', {
				dataTypes : selectedDataTypes,
				startTime : that.startTime,
				endTime : that.endTime
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
			});
		}

	}
});