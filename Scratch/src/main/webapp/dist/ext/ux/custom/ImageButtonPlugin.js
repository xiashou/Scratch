Ext.define('Ext.ux.custom.ImageButtonPlugin', {
	extend : 'Ext.util.Observable',
	alias : 'widget.imagehtmleditor',
	langTitle : '插入图片',
	langIconCls : 'image',
	init : function(view) {
		var scope = this;
		view.on('render', function() {
			scope.onRender(view);
		});
	},

	/**
	 * 添加"插入图片"按钮
	 */
	onRender : function(view) {
		var scope = this;
		view.getToolbar().insert(1, {
			iconCls : scope.langIconCls,
			tooltip : {
				title : scope.langTitle,
				width : 160,
				text : '上传本地图片或媒体图片'
			},
			handler : function() {
				scope.showImgWindow(view);
			}
		});
	},

	/**
	 * 显示"插入图片"窗体
	 */
	showImgWindow : function(view) {
		var scope = this;
		var imgUrl;
		var uploadForm = Ext.create('Ext.form.FormPanel', {
			name : 'uploadForm',
			padding: '5 5 5 5',
			frame : false,
			items : [{
				xtype: 'filefield',
				fieldLabel : '请选择文件',
				buttonText: '浏览...',
				labelAlign: 'right',
				labelWidth: 80,
				name: 'upload',
				allowBlank:false,
				anchor : '99%'
			}]
		});
		Ext.create('Ext.window.Window', {
			id: 'instWin',
			width : 380,
			height : 120,
			title : scope.langTitle,
			layout : 'fit',
			autoShow : true,
			resizable : false,
			maximizable : false,
			constrain : true,
			enableTabScroll : true,
			border : false,
			modal : false,
			collapsible : true,
			titleCollapse : true,
			maximizable : false,
			buttonAlign : 'right',
			border : false,
			animCollapse : true,
			constrain : true,
			items : [uploadForm],
			buttons : [{
				text : '上 传',
				iconCls : 'upload',
				handler : function() {
					uploadForm.form.submit({
						url : '/sys/upload.atc',
						params : {
							mode : 'activity'
						},
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							scope.insertImg(view, action.result.uploadFileName);
							Ext.getCmp("instWin").hide();
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.MessageBox.alert('提示', '上传失败:' + msg);
							uploadForm.form.reset();
						}
					});
				}
			}, {
				text : '关 闭',
				iconCls : 'stop',
				handler : function() {
					Ext.getCmp("instWin").hide();
				}
			}]
		});
		
	},
	
	/**
	 * 上传图片验证
	 */
	uploadImgCheck : function(fileObj, fileName) {
		var scope = this;
		// 图片类型验证
		if (!(scope.getImgTypeCheck(scope.getImgHZ(fileName)))) {
			Ext.MessageBox.show({
	           title: '提示',
	           msg: "上传图片类型有误！",
	           buttons: Ext.MessageBox.OK,
	           icon: Ext.MessageBox.ERROR
            });
			fileObj.reset();// 清空上传内容
			return;
		}
	},

	/**
	 * 获取图片后缀(小写)
	 */
	getImgHZ : function(imgName) {
		// 后缀
		var hz = '';
		// 图片名称中最后一个.的位置
		var index = imgName.lastIndexOf('.');
		if (index != -1) {
			// 后缀转成小写
			hz = imgName.substr(index + 1).toLowerCase();
		}
		return hz;
	},

	/**
	 * 图片类型验证
	 */
	getImgTypeCheck : function(hz) {
		var typestr = 'jpg,jpeg,png,gif,bmp';
		var types = typestr.split(',');// 图片类型
		for (var i = 0; i < types.length; i++) {
			if (hz == types[i]) {
				return true;
			}
		}
		return false;
	},

	/**
	 * 插入图片
	 */
	insertImg : function(view, data) {
		var url = data;
//		var content = data.content;
//		var width = data.width;
//		var height = data.height;
		var str = '<img src="/upload/wechat/activity/' + url + '" border="0" ';
//		if (content != undefined && content != null && content != '') {
//			str += ' title="' + content + '" ';
//		}
//		if (width != undefined && width != null && width != 0) {
//			str += ' width="' + width + '" ';
//		}
//		if (height != undefined && height != null && height != 0) {
//			str += ' height="' + height + '" ';
//		}
		str += ' />';
		view.insertAtCursor(str);
		Ext.getCmp("instWin").close();
	}
});
