var cardCardInfoVM = new Vue({
	el : '#cardCardInfo',
	data : {
		global : GLOBAL,
		bankInfo : '',
		showBankCardInfoFlag : true,

		editBankInfoFlag : false,
		openAccountBank : '',
		accountHolder : '',
		bankCardAccount : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '银行卡信息';
		headerVM.showBackFlag = true;
		this.loadBankInfo();
	},
	methods : {

		loadBankInfo : function() {
			var that = this;
			that.$http.get('/userAccount/getBankInfo').then(function(res) {
				that.bankInfo = res.body.data;
			});
		},

		hideEditBankInfoPage : function() {
			headerVM.showBackFlag = true;
			headerVM.title = '银行卡信息';
			this.showBankCardInfoFlag = true;
			this.editBankInfoFlag = false;
		},

		showEditBankInfoPage : function() {
			headerVM.showBackFlag = false;
			headerVM.title = '绑定银行卡';
			this.showBankCardInfoFlag = false;
			this.editBankInfoFlag = true;
			this.openAccountBank = this.bankInfo.openAccountBank;
			this.accountHolder = this.bankInfo.accountHolder;
			this.bankCardAccount = this.bankInfo.bankCardAccount;
		},

		bindBankInfo : function() {
			var that = this;
			if (that.openAccountBank == null || that.openAccountBank == '') {
				layer.alert('请输入开户银行', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.accountHolder == null || that.accountHolder == '') {
				layer.alert('请输入开户人姓名', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.bankCardAccount == null || that.bankCardAccount == '') {
				layer.alert('请输入银行卡账号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/bindBankInfo', {
				openAccountBank : that.openAccountBank,
				accountHolder : that.accountHolder,
				bankCardAccount : that.bankCardAccount
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('银行卡信息绑定成功!', {
					icon : 1,
					time : 2000,
					shade : false
				});
				that.loadBankInfo();
				that.hideEditBankInfoPage();
			});
		}

	}
});