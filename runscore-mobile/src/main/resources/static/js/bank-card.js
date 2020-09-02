var bankCardVM = new Vue({
	el : '#bankCard',
	data : {
		global : GLOBAL,
		pageNum : 1,
		totalPage : 1,
		bankCards : [],
		showBankCardFlag : true,

		editBankCard : {},
		showEditBankCardFlag : false,
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '我的银行卡';
		headerVM.showBackFlag = true;
		this.loadByPage();
	},
	methods : {

		query : function() {
			this.pageNum = 1;
			this.loadByPage();
		},

		prePage : function() {
			this.pageNum = this.pageNum - 1;
			this.loadByPage();
		},

		nextPage : function() {
			this.pageNum = this.pageNum + 1;
			this.loadByPage();
		},

		loadByPage : function() {
			var that = this;
			that.$http.get('/bankCard/findMyBankCard').then(function(res) {
				that.bankCards = res.body.data;
			});
		},

		showEditBankCardPage : function(id) {
			var that = this;
			if (id != null && id != '') {
				that.$http.get('/bankCard/findMyBankCardById', {
					params : {
						id : id
					}
				}).then(function(res) {
					headerVM.showBackFlag = false;
					headerVM.title = '编辑银行卡';
					that.showBankCardFlag = false;
					that.showEditBankCardFlag = true;
					that.editBankCard = res.body.data;
				});
			} else {
				headerVM.showBackFlag = false;
				headerVM.title = '新增银行卡';
				that.showBankCardFlag = false;
				that.showEditBankCardFlag = true;
				that.editBankCard = {
					openAccountBank : '',
					accountHolder : '',
					bankCardAccount : ''
				};
			}
		},

		hideEditBankCardPage : function() {
			headerVM.showBackFlag = true;
			headerVM.title = '我的银行卡';
			this.showBankCardFlag = true;
			this.showEditBankCardFlag = false;
		},

		addOrUpdateBankCard : function() {
			var that = this;
			var editBankCard = that.editBankCard;
			if (editBankCard.accountHolder == null || editBankCard.accountHolder == '') {
				layer.alert('请输入开户人', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editBankCard.openAccountBank == null || editBankCard.openAccountBank == '') {
				layer.alert('请输入开户银行', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editBankCard.bankCardAccount == null || editBankCard.bankCardAccount == '') {
				layer.alert('请输入银行卡号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/bankCard/addOrUpdateBankCard', that.editBankCard, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.hideEditBankCardPage();
				that.query();
			});
		},

		delBankCard : function(id) {
			var that = this;
			that.$http.get('/bankCard/delBankCard', {
				params : {
					id : id
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.hideEditBankCardPage();
				that.query();
			});
		}
	}
});