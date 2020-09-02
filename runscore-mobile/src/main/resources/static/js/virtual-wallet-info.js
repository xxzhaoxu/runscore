var virtualWalletInfoVM = new Vue({
	el : '#virtualWalletInfo',
	data : {
		global : GLOBAL,
		virtualWalletInfo : '',
		showVirtualWalletInfoFlag : true,

		editVirtualWalletInfoFlag : false,
		virtualWalletAddr : '',
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '电子钱包';
		headerVM.showBackFlag = true;
		this.loadVirtualWalletInfo();
	},
	methods : {

		loadVirtualWalletInfo : function() {
			var that = this;
			that.$http.get('/userAccount/getVirtualWalletInfo').then(function(res) {
				that.virtualWalletInfo = res.body.data;
			});
		},

		hideEditVirtualWalletPage : function() {
			headerVM.showBackFlag = true;
			headerVM.title = '电子钱包';
			this.showVirtualWalletInfoFlag = true;
			this.editVirtualWalletInfoFlag = false;
		},

		showEditVirtualWalletInfoPage : function() {
			headerVM.showBackFlag = false;
			headerVM.title = '绑定电子钱包';
			this.showVirtualWalletInfoFlag = false;
			this.editVirtualWalletInfoFlag = true;
			this.virtualWalletAddr = this.virtualWalletInfo.virtualWalletAddr;
		},

		bindVirtualWallet : function() {
			var that = this;
			if (that.virtualWalletAddr == null || that.virtualWalletAddr == '') {
				layer.alert('请输入钱包地址', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			that.$http.post('/userAccount/bindVirtualWallet', {
				virtualWalletAddr : that.virtualWalletAddr
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('电子钱包绑定成功!', {
					icon : 1,
					time : 2000,
					shade : false
				});
				that.loadVirtualWalletInfo();
				that.hideEditVirtualWalletPage();
			});
		}
	}
});