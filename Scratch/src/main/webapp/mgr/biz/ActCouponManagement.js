/*
 * 活动优惠券管理
 * xs
 * 2018/04/24
 */
Ext.define('Business.ActCouponManagement', {
	extend: 'Ext.ux.desktop.Module',

    requires: [
        'Ext.util.Format',
        'Ext.grid.Panel',
        'Ext.grid.RowNumberer'
    ],

    id:'actcoupon-mgr',

    init : function(){
        this.launcher = {
            text: '优惠券管理',
            iconCls:'coupon'
        };
    },
    
    storeId: -1,
    
	sStore: new Ext.data.Store({
		fields: ['id', 'name'],
		proxy : {
			type : 'ajax',
			url : '/biz/store/queryListByConfig.atc',
			reader : {
				root : 'storeList'
			}
		}
	}),
	
	cStore: new Ext.data.Store({
		pageSize : 200,
		fields: ['id', 'storeId', 'name', 'number', 'endDate', 'createdTime'],
		proxy : {
			type : 'ajax',
			url : '/biz/coupon/queryListPage.atc',
			reader : {
				root : 'couponList',
				totalProperty: 'totalCount'
			}
		}
	}),
    
    store: new Ext.data.Store({
		pageSize : 20,
		fields: ['id', 'actId', 'couponId', 'couponName', 'number', 'endDate', 'createdTime'],
		proxy : {
			type : 'ajax',
			url : '/biz/actCoupon/queryListPage.atc',
			reader : {
				root : 'actCouponList',
				totalProperty: 'totalCount'
			}
		}
	}),
	
	sGrid: function(me) {
		return new Ext.grid.GridPanel({
			border: false,
            store: me.sStore,
            stripeRows : true,
            frame : false,
            forceFit: true,
            viewConfig : {enableTextSelection : true},
    		loadMask : {msg : '正在加载表格数据,请稍等...'},
    		columns: [new Ext.grid.RowNumberer({
            	header : 'No',
        		width : '18%'
            }), {
        		dataIndex : 'id',
        		hidden: true
            },{
            	text : '商户',
            	dataIndex : 'name',
        		width : '82%'
            }],
            listeners:{
		        itemclick: function(grid, record, item, index, e) {
		        	me.storeId = record.data.id;
		        	me.cStore.load({
						params: {
							'coupon.storeId': me.storeId
						}
					});
		        }
		    }
		})
	},
	
	cGrid: function(me) {
		return new Ext.grid.GridPanel({
			border: false,
            store: me.cStore,
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
        		width : '30%'
            },{
            	text : '剩余数量',
            	dataIndex : 'number',
            	width : '15%'
            },{
            	text : '到期日期',
            	dataIndex : 'endDate',
            	width : '20%'
            },{
            	text : '创建时间',
            	dataIndex : 'createdTime',
            	width : '30%'
            }],
            listeners:{
		        itemdblclick: function(grid, record, item, index, e) {
		        	Ext.MessageBox.prompt('请输入需要添加的数量', '输入数量（不能超过'+record.data.number+'）:', function(btn, text){
		        		if(btn=='ok'){
		        			if(Ext.isNumeric(text)){
		        				if(Number(text) <= record.data.number)
		        					me.insertActCoupon(me, record.data.id, Number(text), record.data.endDate);
		        				else
		        					Ext.MessageBox.alert('提示', '数量不能大于'+record.data.number+'！');
		        			} else
		        				Ext.MessageBox.alert('提示', '请输入数字!');
		        		}
		        	});
		        }
		    }
		})
	},
	
	insertActCoupon: function(me, couponId, number, endDate){
		var desktop = me.app.getDesktop();
		Ext.Ajax.request({
            method:'POST',
            url: '/biz/actCoupon/insertActCoupon.atc',
            params : {
				'actCoupon.couponId' : couponId,
				'actCoupon.number' : number,
				'actCoupon.endDate' : endDate
			},
			success : function(resp, opts) {
				var result = Ext.decode(resp.responseText);
				if (result.success) {
					desktop.showMessage(result.msg);
					me.selModel.deselectAll();
					me.store.reload();
					me.cStore.reload();
				} else
					Ext.MessageBox.show({
						title : '提示',
						msg : result.msg,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.ERROR
					});
			},
			failure : function(resp, opts) {
				Ext.MessageBox.hide();
				var result = Ext.decode(resp.responseText);
                Ext.Msg.alert('提示', result.msg);
			}
        });
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
            	subwin_actcouponmgr.setTitle('选择（双击添加）');
            	subwin_actcouponmgr.show();
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
        					url : '/biz/actCoupon/deleteActCoupon.atc',
        					params : {
        						'actCoupon.id' : record.data.id
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
        var win = desktop.getWindow('actcoupon-mgr');
        
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
                id: 'actcoupon-mgr',
                title:'优惠券管理',
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
	                    	dataIndex : 'actId',
	                    	hidden: true
	                    },{
	                    	dataIndex : 'couponId',
	                    	hidden: true
	                    },{
	                    	text : '优惠券名称',
	                    	dataIndex : 'couponName',
	                		width : '30%'
	                    },{
	                    	text : '数量',
	                    	dataIndex : 'number',
	                    	width : '20%'
	                    },{
	                    	text : '到期日期',
	                		dataIndex : 'endDate',
	                		width : '20%'
	                    },{
	                    	text : '创建时间',
	                    	dataIndex : 'createdTime',
	                    	width : '20%'
	                    }]
                    }, subwin_actcouponmgr = Ext.create('Ext.Window', {
                        width: 720,
                        height: 500,
                        constrain: false,
                        layout : 'border',
                        closeAction: 'hide',
//                        layout: 'fit',
                        items: [{
                        	collapsible : true,
                			width : '25%',
                			split : true,
                			region : 'west',
                			layout : 'fit',
                			border : false,
                			title : '商户',
                			items : [ me.sGrid(me) ]
                        },{
                			width : '75%',
                			split : true,
                			region : 'center',
                			layout : 'fit',
                			border : false,
                			title : '优惠券',
                			items : [ me.cGrid(me) ]
                        }],
                        listeners: {
                            show: function() {
                            	me.sStore.load();
                            }
                        }
                        //buttons: me.buttons(me)
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

