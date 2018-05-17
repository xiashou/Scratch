/*
 * 商户优惠券管理
 * xs
 * 2018/04/24
 */
Ext.define('Business.CouponManagement', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'coupon-mgr',

    init : function(){
        this.launcher = {
            text: '优惠券管理',
            iconCls:'coupon'
        };
    },
    
    store: new Ext.data.Store({
		pageSize : 20,
		fields: ['id', 'storeId', 'name', 'describ', 'number', 'imageUrl', 'endDate', 'createdTime'],
		proxy : {
			type : 'ajax',
			url : '/biz/coupon/queryListPage.atc',
			reader : {
				root : 'couponList',
				totalProperty: 'totalCount'
			}
		}
	}),
	
	bbar: function(me) {
		var desktop = me.app.getDesktop();
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
	  	return new Ext.PagingToolbar({
	  		pageSize : number,
	  		store : me.store,
	  		displayInfo : true,
	  		displayMsg : '显示{0}条到{1}条,共{2}条',
	  		emptyMsg : "没有符合条件的记录",
	  		items : [ '-', '&nbsp;&nbsp;', pagesizeCombo ]
	  	});
	},
	
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
				name : 'coupon.name',
				fieldLabel : '名称',
			},{
				name : 'coupon.number',
				fieldLabel : '数量',
				xtype: 'numberfield'
			},{
				name : 'coupon.storeId',
				xtype: 'hiddenfield'
			},{
				name : 'coupon.id',
				xtype: 'hiddenfield'
			}]
		},{
			defaults : {flex : 1,xtype : 'textarea',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				name: 'coupon.describ',
				fieldLabel : '描述',
				rows: 2
			} ]
		},{
			defaults : {flex : 5,xtype : 'filefield',labelWidth : 70,labelAlign : 'right'},
			items : [ {
				fieldLabel : '图片',
				name: 'image',
				buttonText: '浏览...',
				anchor : '99%'
			},{
				xtype : 'displayfield',
				flex : 2,
				labelWidth : 5,
				fieldLabel : '&nbsp;',
				labelSeparator: '',
				value:'最佳尺寸80*80'
			} ]
		},{
			defaults : {flex : 1,xtype : 'datefield',labelWidth : 70,labelAlign : 'right'},
			items: [{
				name: 'coupon.endDate',
				fieldLabel: '到期日期',
				format:'Y/m/d'
			},{
				xtype : 'displayfield',
				fieldLabel : '&nbsp;',
				labelSeparator: '',
				value:''
			}]
		} ]
	}),
	
	buttons: function(me){
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
							subwin_couponmgr.hide();
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
				subwin_couponmgr.hide();
			}
		}];
	},
	
	imageRenderer: function(value, metaData, record) {
		if(value){
  			metaData.tdAttr = "data-qtip=\"<img src='/upload/coupon/" + value + "' style='width:100px; height:100px'/>\""; 
  			return '<img src="/upload/coupon/' + value + '" style="height:15px" onerror="this.src=\'/resources/img/noImage.png\'" />';
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
            	subwin_couponmgr.setTitle('新建优惠券');
            	me.f.getForm().reset();
            	me.f.getForm().findField('coupon.storeId').setValue(BB);
            	me.f.getForm().url = '/biz/coupon/insertCoupon.atc';
            	subwin_couponmgr.show();
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
        		subwin_couponmgr.setTitle('修改优惠券');
        		me.f.getForm().reset();
            	me.f.getForm().url = '/biz/coupon/updateCoupon.atc';
            	me.selModel.deselectAll();
//            	me.f.loadRecord(record);
            	me.f.getForm().setValues([
            		{id:'coupon.id', value:record.data.id},
					{id:'coupon.storeId', value:BB},
					{id:'coupon.name', value:record.data.name},
					{id:'coupon.describ', value:record.data.describ},
					{id:'coupon.number', value:record.data.number},
					{id:'coupon.endDate', value:record.data.endDate}
				]);
        		subwin_couponmgr.show();
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
        					url : '/biz/coupon/deleteCoupon.atc',
        					params : {
        						'coupon.id' : record.data.id
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
        }]
	},

    createWindow : function(){
    	var me = this;
        var desktop = me.app.getDesktop();
        var win = desktop.getWindow('coupon-mgr');
        
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
                id: 'coupon-mgr',
                title:'优惠券管理',
                width:900,
                height:580,
                iconCls: 'coupon',
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
	                		width : '5%'
	                    }), {
	                		dataIndex : 'id',
	                		hidden: true
	                    },{
	                    	text : '名称',
	                    	dataIndex : 'name',
	                		width : '20%'
	                    },{
	                    	text : '描述',
	                    	dataIndex : 'describ',
	                    	width : '20%'
	                    },{
	                    	text : '数量',
	                		dataIndex : 'number',
	                		width : '10%'
	                    },{
	                    	text : '图片',
	                    	dataIndex : 'imageUrl',
	                    	width : '10%',
	                    	renderer: me.imageRenderer
	                    },{
	                    	text : '到期日期',
	                    	dataIndex : 'endDate',
	                    	width : '15%'
	                    },{
	                    	text : '创建时间',
	                    	dataIndex : 'createdTime',
	                    	width : '20%'
	                    }]
                    }, subwin_couponmgr = Ext.create('Ext.Window', {
                        width: 460,
                        height: 250,
                        constrain: true,
                        layout: 'fit',
                        closeAction: 'hide',
                        items: [me.f],
                        buttons: me.buttons(me)
                    })
                ],
                tbar: me.tbar(me),
                bbar: bbar,
                listeners: {
                    show: function() {
                    	me.store.load({
                    		params: {
                    			'coupon.storeId': BB
                    		}
                    	});
                    }
                }
            });
        }
        return win;
    }

});

