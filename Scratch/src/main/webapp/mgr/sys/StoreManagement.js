/*
 * 商户管理
 * xs
 * 2018/04/03
 */
Ext.define('System.StoreManagement', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'store-mgr',

    init : function(){
        this.launcher = {
            text: '商户管理',
            iconCls:'store'
        };
    },
    
    store: new Ext.data.Store({
		pageSize : 20,
		fields: ['id', 'appid', 'name', 'address', 'phone', 'introduction', 'headUrl', 'imageUrl', 'locationx', 'locationy', 'enable', 'createdTime'],
		proxy : {
			type : 'ajax',
			url : '/biz/store/queryListPage.atc',
			reader : {
				root : 'storeList',
				totalProperty: 'totalCount'
			}
		}
	}),
	
	f: new Ext.form.FormPanel({
		layout : 'anchor',
		defaults : {
			anchor : '100%',
			layout : 'hbox',
			xtype : 'fieldcontainer'
		},
		bodyPadding : '5 10 0 0',
		border : false,
		items : [ {
			defaults : {flex : 1,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				name : 'store.name',
				fieldLabel : '商户名称',
			},{
				name : 'store.phone',
				fieldLabel : '联系电话',
			},{
				name : 'store.id',
				xtype: 'hiddenfield'
			}]
		},{
			defaults : {flex : 1,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
			items : [{
				name: 'store.address',
				fieldLabel : '地址'
			}]
		},{
			defaults : {flex : 8,xtype : 'textfield',labelWidth : 70,labelAlign : 'right'},
			items: [ {
				name: 'store.locationx',
				fieldLabel : '地址x坐标',
				readOnly:true, 
				fieldStyle:'background-color: #F0F0F0;'
			},{
				name: 'store.locationy',
				fieldLabel : '地址y坐标',
				readOnly:true, 
				fieldStyle:'background-color: #F0F0F0;'
			},{
				xtype: 'button',
				flex : 3,
				margin: '0 0 0 5',
				text: '选取地址',
				handler: function() {
					mapwin_storemgr.update('<iframe src="/mgr/ux/map/map.jsp" width="100%" height="100%" frameborder="0"></iframe>');
					mapwin_storemgr.show();
			    }
			}]
		},{
			defaults : {flex : 1,xtype : 'textarea',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				fieldLabel : '商户简介',
				name: 'store.introduction',
				rows: 3
			} ]
		},{
			defaults : {flex : 3,xtype : 'filefield',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				fieldLabel : '小图',
				name: 'head',
				buttonText: '浏览...',
				anchor : '99%'
			},{
				xtype : 'displayfield',
				flex : 1,
				labelWidth : 5,
				fieldLabel : '&nbsp;',
				labelSeparator: '',
				value:'最佳尺寸180*180'
			} ]
		},{
			defaults : {flex : 3,xtype : 'filefield',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				fieldLabel : '大图',
				name: 'image',
				buttonText: '浏览...',
				anchor : '99%'
			},{
				xtype : 'displayfield',
				flex : 1,
				labelWidth : 5,
				fieldLabel : '&nbsp;',
				labelSeparator: '',
				value:'最佳尺寸750*408'
			} ]
		},{
			defaults : {flex : 1,xtype : 'radiogroup',labelWidth : 70,labelAlign : 'right'},
			items: [{
				fieldLabel: '是否启用', 
				defaults: {name: 'store.enable'},
	        	items: [{inputValue: true, boxLabel: '启用', checked: true}, {inputValue: false,boxLabel: '关闭'}]
			},{
				xtype : 'displayfield',
				flex : 1,
				fieldLabel : '&nbsp;',
				labelSeparator: '',
				value:''
			}]
		} ]
	}),
	
	buttons: function(me) {
		var desktop = me.app.getDesktop();
		return [{
			text : '保 存',
			iconCls : 'accept',
			handler : function() {
				if (me.f.form.isValid()) {
					me.f.form.submit({
						waitTitle : '提示',
						method : 'POST',
						waitMsg : '正在处理数据,请稍候...',
						success : function(form, action) {
							subwin_storemgr.hide();
							desktop.showMessage(action.result.msg);
							me.store.reload();
						},
						failure : function(form, action) {
							var msg = action.result.msg;
							Ext.MessageBox.alert('提示', msg);
						}
					});
				}
			}
		}, {
			text : '关 闭 ',
			iconCls : 'stop',
			handler : function() {
				mapwin_storemgr.hide();
				subwin_storemgr.hide();
			}
		}];
	},
	
	smallRenderer: function(value, metaData, record) {
		if(value){
  			metaData.tdAttr = "data-qtip=\"<img src='/upload/store/" + record.data.appid + '/' + value + "' style='width:90px; height:90px'/>\""; 
  			return '<img src="/upload/store/' + record.data.appid + '/' + value + '" style="height:20px" onerror="this.src=\'/resources/img/noImage.png\'" />';
  		} else
  			return '<img src="/resources/img/noImage.png" style="height:20px" />';
    },
    
    largeRenderer: function(value, metaData, record) {
		if(value){
  			metaData.tdAttr = "data-qtip=\"<img src='/upload/store/" + record.data.appid + '/' + value + "' style='width:375px; height:204px'/>\""; 
  			return '<img src="/upload/store/' + record.data.appid + '/' + value + '" style="height:20px" onerror="this.src=\'/resources/img/noImage.png\'" />';
  		} else
  			return '<img src="/resources/img/noImage.png" style="height:20px" />';
    },
  	
  	selModel: Ext.create('Ext.selection.CheckboxModel', {
		injectCheckbox : 1,
		mode : 'SINGLE'
	}),
	
	tbar: function(me) {
		var desktop = me.app.getDesktop();
		return [{
            text:'新增',
            iconCls:'add',
            handler : function() {
            	subwin_storemgr.setTitle('新建商户');
            	me.f.getForm().reset();
            	me.f.getForm().url = '/biz/store/insertStore.atc';
            	subwin_storemgr.show();
			}
        }, {
            text:'修改',
            iconCls:'pencil',
            handler : function() {
            	var record = me.selModel.getSelection()[0];
        		if (Ext.isEmpty(record)) {
        			Ext.MessageBox.show({
        				title : '提示',
        				msg : '你没有选中任何项目！',
        				buttons : Ext.MessageBox.OK,
        				icon : Ext.MessageBox.INFO
        			});
        			return;
        		}
        		subwin_storemgr.setTitle('修改商户');
            	me.f.getForm().reset();
            	me.f.getForm().url = '/biz/store/updateStore.atc';
            	me.selModel.deselectAll();
//            	me.f.loadRecord(record);
            	me.f.getForm().setValues([
            		{id:'store.id', value:record.data.id},
					{id:'store.name', value:record.data.name},
					{id:'store.address', value:record.data.address},
					{id:'store.phone', value:record.data.phone},
					{id:'store.introduction', value:record.data.introduction},
					{id:'store.locationx', value:record.data.locationx},
					{id:'store.locationy', value:record.data.locationy},
					{id:'store.enable', value:record.data.enable}
				]);
        		subwin_storemgr.show();
			}
        }, {
            text:'删除',
            iconCls:'delete',
            handler: function(){
            	var record = me.selModel.getSelection()[0];
        		if (Ext.isEmpty(record)) {
        			Ext.MessageBox.show({
        				title : '提示',
        				msg : '你没有选中任何项目！',
        				buttons : Ext.MessageBox.OK,
        				icon : Ext.MessageBox.INFO
        			});
        			return;
        		}
        		Ext.Msg.confirm('请确认', '确定要删除这项吗?', function(btn, text) {
        			if (btn == 'yes') {
        				Ext.Ajax.request({
        					url : '/biz/store/deleteStore.atc',
        					params : {
        						'store.id' : record.data.id
        					},
        					success : function(resp, opts) {
        						var result = Ext.decode(resp.responseText);
        						if (result.success) {
        							desktop.showMessage(result.msg);
        							me.selModel.deselectAll();
        							me.store.reload();
        						} else
        							Ext.MessageBox.show({
        								title : '提示',
        								msg : result.msg,
        								buttons : Ext.MessageBox.OK,
        								icon : Ext.MessageBox.ERROR
        							});
        					},
        					failure : function(resp, opts) {
        						var result = Ext.decode(resp.responseText);
        						Ext.MessageBox.show({
        							title : '提示',
        							msg : result.msg,
        							buttons : Ext.MessageBox.OK,
        							icon : Ext.MessageBox.ERROR
        						});
        					}
        				});
        			}
        		});
            }
        }];
	},

    createWindow : function(){
    	var me = this;
        var desktop = me.app.getDesktop();
        var win = desktop.getWindow('store-mgr');
        
        var pagesizeCombo = desktop.getPagesizeCombo();
        var number = parseInt(pagesizeCombo.getValue());
	  	pagesizeCombo.on("select", function(comboBox) {
	  		bbar.pageSize = parseInt(comboBox.getValue());
	  		number = parseInt(comboBox.getValue());
	  		me.store.pageSize = parseInt(comboBox.getValue());
	  		me.store.reload({
	  			params : {
	  				start : 0,
	  				limit : bbar.pageSize
	  			}
	  		});
	  	});
	  	
	  	var bbar = new Ext.PagingToolbar({
	  		pageSize : number,
	  		store : me.store,
	  		displayInfo : true,
	  		displayMsg : '显示{0}条到{1}条,共{2}条',
	  		emptyMsg : "没有符合条件的记录",
	  		items : [ '-', '&nbsp;&nbsp;', pagesizeCombo ]
	  	});
	  	
        if(!win){
            win = desktop.createWindow({
                id: 'store-mgr',
                title:'商户管理',
                width:900,
                height:580,
                iconCls: 'store',
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                items: [{
                	border: false,
                    xtype: 'grid',
                    store: me.store,
                    selModel: me.selModel,
                    stripeRows : true,
                    frame : false,
                    forceFit: true,
                    viewConfig : {enableTextSelection : true},
            		loadMask : {msg : '正在加载表格数据,请稍等...'},
            		columns: [new Ext.grid.RowNumberer({
	                    	header : 'No',
	                		width : '4%'
	                    }), {
	                		dataIndex : 'id',
	                		hidden: true
	                    },{
	                    	text : '店名',
	                    	dataIndex : 'name',
	                		width : '15%'
	                    },{
	                    	text : '联系电话',
	                    	dataIndex : 'phone',
	                    	width : '12%'
	                    },{
	                    	text : '地址',
	                		dataIndex : 'address',
	                		width : '25%'
	                    },{
	                    	text : '小图',
	                    	dataIndex : 'headUrl',
	                    	width : '8%',
	                    	renderer: me.smallRenderer
	                    },{
	                    	text : '大图',
	                    	dataIndex : 'imageUrl',
	                    	width : '8%',
	                    	renderer: me.largeRenderer
	                    },{
	                    	text : '是否可用',
	                    	dataIndex : 'enable',
	                    	width : '8%',
	                    	renderer: function(value){
	            				return value ? '<font color=green>正常</font>' : '<font color=red>锁定</font>'
	            			}
	                    },{
	                    	text : '创建时间',
	                    	dataIndex : 'createdTime',
	                    	width : '16%'
	                    }]
                    }, subwin_storemgr = Ext.create('Ext.Window', {
                        width: 520,
                        height: 350,
                        constrain: true,
                        layout: 'fit',
                        closeAction: 'hide',
                        items: [me.f],
                        buttons: me.buttons(me)
                    }), mapwin_storemgr = Ext.create('Ext.Window', {
                    	width: 635,
                        height: 610,
                        title:'选取地址',
                        constrain: false,
						closeAction:'hide',
                        layout: 'fit'
                    })
                ],
                tbar:me.tbar(me),
                bbar:bbar,
                listeners: {
                    show: function() {
                    	me.store.load();
                    }
                }
            });
        }
        return win;
    }

});

