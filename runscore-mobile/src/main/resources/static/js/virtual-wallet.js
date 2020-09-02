var virtualWalletVM = new Vue({
	el : '#virtualWallet',
	data : {
		global : GLOBAL,
		pageNum : 1,
		totalPage : 1,
		virtualWallets : [],
		showVirtualWalletFlag : true,

		editVirtualWallet : {},
		showEditVirtualWalletFlag : false,
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '我的电子钱包';
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
			that.$http.get('/virtualWallet/findMyVirtualWallet').then(function(res) {
				that.virtualWallets = res.body.data;
			});
		},

		showEditVirtualWalletPage : function(id) {
			var that = this;
			if (id != null && id != '') {
				that.$http.get('/virtualWallet/findMyVirtualWalletById', {
					params : {
						id : id
					}
				}).then(function(res) {
					headerVM.showBackFlag = false;
					headerVM.title = '编辑电子钱包';
					that.showVirtualWalletFlag = false;
					that.showEditVirtualWalletFlag = true;
					that.editVirtualWallet = res.body.data;
				});
			} else {
				headerVM.showBackFlag = false;
				headerVM.title = '新增电子钱包';
				that.showVirtualWalletFlag = false;
				that.showEditVirtualWalletFlag = true;
				that.editVirtualWallet = {
					virtualWalletType : '',
					virtualWalletAddr : ''
				};
			}
		},

		hideEditVirtualWalletPage : function() {
			headerVM.showBackFlag = true;
			headerVM.title = '我的电子钱包';
			this.showVirtualWalletFlag = true;
			this.showEditVirtualWalletFlag = false;
		},

		addOrUpdateVirtualWallet : function() {
			var that = this;
			var editVirtualWallet = that.editVirtualWallet;
			if (editVirtualWallet.virtualWalletType == null || editVirtualWallet.virtualWalletType == '') {
				layer.alert('请输入钱包类型', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editVirtualWallet.virtualWalletAddr == null || editVirtualWallet.virtualWalletAddr == '') {
				layer.alert('请输入钱包地址', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/virtualWallet/addOrUpdateVirtualWallet', that.editVirtualWallet, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.hideEditVirtualWalletPage();
				that.query();
			});
		},

		delVirtualWallet : function(id) {
			var that = this;
			that.$http.get('/virtualWallet/delVirtualWallet', {
				params : {
					id : id
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.hideEditVirtualWalletPage();
				that.query();
			});
		}
	}
});