var platformVM = new Vue({
	el : '#platform',
	data : {
		accountStateDictItems : [],
		name : '',
		modifyLoginPwdFlag : false,
		selectedMerchant : {},
		newLoginPwd : '',
		addMerchantFlag : false,
		merchantEditFlag : false,
		editMerchant : {},

		modifyMoneyPwdFlag : false,
		newMoneyPwd : '',

		rateFlag : false,
		rates : [],
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.loadAccountStateItem();
		this.loadGatheringChannelDictItem();
		this.initTable();
	},
	methods : {

		loadAccountStateItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'accountState'
				}
			}).then(function(res) {
				this.accountStateDictItems = res.body.data;
			});
		},

		loadGatheringChannelDictItem : function() {
			var that = this;
			that.$http.get('/gatheringChannel/findAllGatheringChannel').then(function(res) {
				that.gatheringChannelDictItems = res.body.data;
			});
		},

		initTable : function() {
			var that = this;
			$('.platform-table').bootstrapTable(
					{
						classes : 'table table-hover',
						url : '/merchant/findMerchantByPage',
						pagination : true,
						sidePagination : 'server',
						pageNumber : 1,
						pageSize : 10,
						pageList : [ 10, 25, 50, 100 ],
						queryParamsType : '',
						queryParams : function(params) {
							var condParam = {
								pageSize : params.pageSize,
								pageNum : params.pageNumber,
								name : that.name
							};
							return condParam;
						},
						responseHandler : function(res) {
							return {
								total : res.data.total,
								rows : res.data.content
							};
						},
						detailView : true,
						detailFormatter : function(index, row, element) {
							var html = template('merchant-detail', {
								merchantInfo : row
							});
							return html;
						},
						columns : [
								{
									field : 'userName',
									title : '用户名'
								},
								{
									field : 'merchantNum',
									title : '商户号'
								},
								{
									field : 'merchantName',
									title : '商户名称'
								},
								{
									field : 'withdrawableAmount',
									title : '可提现金额'
								},
								{
									field : 'stateName',
									title : '账号状态'
								},
								{
									field : 'createTime',
									title : '创建时间'
								},
								{
									field : 'latelyLoginTime',
									title : '最近登录时间'
								},
								{
									title : '操作',
									formatter : function(value, row, index) {
										return [ '<button type="button" class="merchant-rate-btn btn btn-outline-info btn-sm" style="margin-right: 4px;">通道费率</button>',
												'<button type="button" class="merchant-edit-btn btn btn-outline-primary btn-sm" style="margin-right: 4px;">编辑</button>',
												'<button type="button" class="modify-login-pwd-btn btn btn-outline-secondary btn-sm" style="margin-right: 4px;">修改登录密码</button>',
												'<button type="button" class="modify-money-pwd-btn btn btn-outline-secondary btn-sm" style="margin-right: 4px;">修改资金密码</button>',
												'<button type="button" class="del-merchant-btn btn btn-outline-danger btn-sm">删除</button>' ].join('');
									},
									events : {
										'click .merchant-rate-btn' : function(event, value, row, index) {
											that.showRateModal(row);
										},
										'click .merchant-edit-btn' : function(event, value, row, index) {
											that.showMerchantEditModal(row.id);
										},
										'click .del-merchant-btn' : function(event, value, row, index) {
											that.delMerchant(row.id);
										},
										'click .modify-login-pwd-btn' : function(event, value, row, index) {
											that.showModifyLoginPwdModal(row);
										},
										'click .modify-money-pwd-btn' : function(event, value, row, index) {
											that.showModifyMoneyPwdModal(row);
										}
									}
								} ]
					});
		},

		refreshTable : function() {
			$('.platform-table').bootstrapTable('refreshOptions', {
				pageNumber : 1
			});
		},

		showModifyLoginPwdModal : function(row) {
			this.selectedMerchant = row;
			this.newLoginPwd = '';
			this.modifyLoginPwdFlag = true;
		},

		modifyLoginPwd : function() {
			var that = this;
			if (that.newLoginPwd == null || that.newLoginPwd == '') {
				layer.alert('请输入登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/merchant/modifyLoginPwd', {
				id : that.selectedMerchant.id,
				newLoginPwd : that.newLoginPwd
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.modifyLoginPwdFlag = false;
				that.refreshTable();
			});
		},

		showModifyMoneyPwdModal : function(row) {
			this.selectedMerchant = row;
			this.newMoneyPwd = '';
			this.modifyMoneyPwdFlag = true;
		},

		modifyMoneyPwd : function() {
			var that = this;
			if (that.newMoneyPwd == null || that.newMoneyPwd == '') {
				layer.alert('请输入资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/merchant/modifyMoneyPwd', {
				id : that.selectedMerchant.id,
				newMoneyPwd : that.newMoneyPwd
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.modifyMoneyPwdFlag = false;
				that.refreshTable();
			});
		},

		generateSecretKey : function() {
			var that = this;
			that.$http.get('/merchant/generateSecretKey').then(function(res) {
				that.editMerchant.secretKey = res.body.data;
			});
		},

		showAddMerchantModal : function() {
			this.addMerchantFlag = true;
			this.editMerchant = {
				userName : '',
				loginPwd : '',
				state : '',
				merchantNum : '',
				merchantName : '',
				secretKey : '',
				notifyUrl : '',
				returnUrl : ''
			}
		},

		showMerchantEditModal : function(id) {
			var that = this;
			that.$http.get('/merchant/findMerchantById', {
				params : {
					id : id
				}
			}).then(function(res) {
				that.editMerchant = res.body.data;
				that.merchantEditFlag = true;
			});
		},

		addMerchant : function() {
			var that = this;
			var editMerchant = that.editMerchant;
			if (editMerchant.userName === null || editMerchant.userName === '') {
				layer.alert('请输入用户名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editMerchant.loginPwd === null || editMerchant.loginPwd === '') {
				layer.alert('请输入登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editMerchant.merchantNum === null || editMerchant.merchantNum === '') {
				layer.alert('请输入商户号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editMerchant.merchantName === null || editMerchant.merchantName === '') {
				layer.alert('请输入商户名称', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editMerchant.state === null || editMerchant.state === '') {
				layer.alert('请选择状态', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editMerchant.secretKey === null || editMerchant.secretKey === '') {
				layer.alert('请输入接入密钥', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/merchant/addMerchant', editMerchant, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.addMerchantFlag = false;
				that.refreshTable();
			});
		},

		updateMerchant : function() {
			var that = this;
			var editMerchant = that.editMerchant;
			if (editMerchant.userName === null || editMerchant.userName === '') {
				layer.alert('请输入用户名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editMerchant.merchantNum === null || editMerchant.merchantNum === '') {
				layer.alert('请输入商户号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editMerchant.name === null || editMerchant.name === '') {
				layer.alert('请输入商户名称', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editMerchant.state === null || editMerchant.state === '') {
				layer.alert('请选择状态', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editMerchant.secretKey === null || editMerchant.secretKey === '') {
				layer.alert('请输入接入密钥', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/merchant/updateMerchant', editMerchant, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.merchantEditFlag = false;
				that.refreshTable();
			});
		},

		delMerchant : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchant/delMerchantById', {
					params : {
						id : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.refreshTable();
				});
			});
		},

		showRateModal : function(row) {
			var that = this;
			that.$http.get('/gatheringChannel/findGatheringChannelRateByMerchantId', {
				params : {
					merchantId : row.id
				}
			}).then(function(res) {
				that.selectedMerchant = row;
				that.rateFlag = true;
				that.rates = res.body.data;
			});
		},

		addRate : function() {
			this.rates.push({
				channelId : '',
				rate : '',
				minAmount : '',
				maxAmount : '',
				enabled : true
			});
		},

		saveRate : function() {
			var that = this;
			var rates = that.rates;
			if (rates.length == 0) {
				layer.alert('请设置费率', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var channelMap = new Map();
			for (var i = 0; i < rates.length; i++) {
				var rate = rates[i];
				if (rate.merchantId === null || rate.merchantId === '') {
					layer.alert('请选择商户', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (rate.channelId === null || rate.channelId === '') {
					layer.alert('请选择通道', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (rate.rate === null || rate.rate === '') {
					layer.alert('请输入费率', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (rate.minAmount === null || rate.minAmount === '') {
					layer.alert('请输入最低限额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (rate.maxAmount === null || rate.maxAmount === '') {
					layer.alert('请输入最高限额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (rate.minAmount > rate.maxAmount) {
					layer.alert('最低限额不能大于最高限额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (rate.enabled === null || rate.enabled === '') {
					layer.alert('请选择是否启用', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				var key = rate.channelId;
				if (channelMap.get(key) != null) {
					layer.alert('不能设置重复的记录', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				channelMap.set(key, key);
			}

			that.$http.post('/gatheringChannel/saveGatheringChannelRate', {
				merchantId : that.selectedMerchant.id,
				channelRates : rates
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.rateFlag = false;
				that.refreshTable();
			});
		}

	}
});