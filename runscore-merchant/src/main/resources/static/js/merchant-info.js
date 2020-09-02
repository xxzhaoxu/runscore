var merchantInfoVM = new Vue({
	el : '#merchant-info',
	data : {
		global : GLOBAL,
		selectedTab : 'merchantInfo',
		merchantInfo : {},
		
		modifyLoginPwdFlag : '',
		oldLoginPwd : '',
		newLoginPwd : '',
		confirmLoginPwd : '',
		
		modifyMoneyPwdFlag : '',
		oldMoneyPwd : '',
		newMoneyPwd : '',
		confirmMoneyPwd : '',

		merchantBankCards : [],
		editBankInfoFlag : false,
		bankCardActionTitle : '',
		editBankInfo : {},

	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		this.getMerchantInfo();
		this.loadMerchantBankCard();
	},
	methods : {

		getMerchantInfo : function() {
			var that = this;
			that.$http.get('/merchant/getMerchantInfo').then(function(res) {
				that.merchantInfo = res.body.data;
			});
		},

		loadMerchantBankCard : function() {
			var that = this;
			that.$http.get('/merchant/findMerchantBankCardByMerchantId').then(function(res) {
				that.merchantBankCards = res.body.data;
			});
		},

		showModifyLoginPwdModal : function() {
			this.oldLoginPwd = '';
			this.newLoginPwd = '';
			this.confirmLoginPwd = '';
			this.modifyLoginPwdFlag = true;
		},

		modifyLoginPwd : function() {
			var that = this;
			if (that.oldLoginPwd == null || that.oldLoginPwd == '') {
				layer.alert('请输入旧的登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newLoginPwd == null || that.newLoginPwd == '') {
				layer.alert('请输入新的登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.confirmLoginPwd == null || that.confirmLoginPwd == '') {
				layer.alert('请输入确认登录密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newLoginPwd != that.confirmLoginPwd) {
				layer.alert('密码不一致', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var passwordPatt = /^[A-Za-z][A-Za-z0-9]{5,14}$/;
			if (!passwordPatt.test(that.newLoginPwd)) {
				layer.alert('登录密码不合法!请输入以字母开头,长度为6-15个字母和数字的密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/merchant/modifyLoginPwd', {
				oldLoginPwd : that.oldLoginPwd,
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
			});
		},

		showModifyMoneyPwdModal : function() {
			this.oldMoneyPwd = '';
			this.newMoneyPwd = '';
			this.confirmMoneyPwd = '';
			this.modifyMoneyPwdFlag = true;
		},

		modifyMoneyPwd : function() {
			var that = this;
			if (that.oldMoneyPwd == null || that.oldMoneyPwd == '') {
				layer.alert('请输入旧的资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newMoneyPwd == null || that.newMoneyPwd == '') {
				layer.alert('请输入新的资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.confirmMoneyPwd == null || that.confirmMoneyPwd == '') {
				layer.alert('请输入确认资金密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.newMoneyPwd != that.confirmMoneyPwd) {
				layer.alert('密码不一致', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			var passwordPatt = /^[A-Za-z][A-Za-z0-9]{5,14}$/;
			if (!passwordPatt.test(that.newMoneyPwd)) {
				layer.alert('资金密码不合法!请输入以字母开头,长度为6-15个字母和数字的密码', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/merchant/modifyMoneyPwd', {
				oldMoneyPwd : that.oldMoneyPwd,
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
			});
		},

		showAddBankInfoModal : function() {
			this.editBankInfoFlag = true;
			this.bankCardActionTitle = '新增银行卡';
			this.editBankInfo = {
				openAccountBank : '',
				accountHolder : '',
				bankCardAccount : ''
			};

		},

		saveBankInfo : function() {
			var that = this;
			var editBankInfo = that.editBankInfo;
			if (editBankInfo.openAccountBank === null || editBankInfo.openAccountBank === '') {
				layer.alert('请输入开户银行', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editBankInfo.accountHolder === null || editBankInfo.accountHolder === '') {
				layer.alert('请输入开户人姓名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editBankInfo.bankCardAccount === null || editBankInfo.bankCardAccount === '') {
				layer.alert('请输入银行卡账号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/merchant/addOrUpdateMerchantBankCard', editBankInfo, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.editBankInfoFlag = false;
				that.loadMerchantBankCard();
			});
		},

		showEditBankCardModal : function(id) {
			var that = this;
			that.$http.get('/merchant/findMerchantBankCardById', {
				params : {
					id : id
				}
			}).then(function(res) {
				this.editBankInfoFlag = true;
				this.bankCardActionTitle = '编辑银行卡';
				this.editBankInfo = res.body.data;
			});
		},

		delBankCard : function(id) {
			var that = this;
			layer.confirm('确定要删除吗?', {
				icon : 7,
				title : '提示'
			}, function(index) {
				layer.close(index);
				that.$http.get('/merchant/deleteMerchantBankCard', {
					params : {
						merchantBankCardId : id
					}
				}).then(function(res) {
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
					that.loadMerchantBankCard();
				});
			});
		}

	}
});