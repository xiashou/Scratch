var Login = function () {

	var handleLogin = function() {
		$('.login-form').validate({
	            errorElement: 'span', //default input error message container
	            errorClass: 'help-block', // default input error message class
	            focusInvalid: false, // do not focus the last invalid input
	            rules: {
	                username: {
	                    required: true
	                },
	                password: {
	                    required: true
	                },
	                remember: {
	                    required: false
	                }
	            },

	            messages: {
	                username: {
	                    required: "用户名不能为空."
	                },
	                password: {
	                    required: "密码不能为空."
	                }
	            },

	            invalidHandler: function (event, validator) { //display error alert on form submit   
	            	$('.alert-danger span').html('请输入账号和密码.');
	                $('.alert-danger', $('.login-form')).show();
	            },

	            highlight: function (element) { // hightlight error inputs
	                $(element)
	                    .closest('.form-group').addClass('has-error'); // set error class to the control group
	            },

	            success: function (label) {
	                label.closest('.form-group').removeClass('has-error');
	                label.remove();
	            },

	            errorPlacement: function (error, element) {
	                error.insertAfter(element.closest('.input-icon'));
	            },

	            submitHandler: function (form) {
	            	$.ajax({
                    	url:"/userLogin.atc",
                    	data:{
                    		'sysUser.userName': $("#username").val(),
                    		'sysUser.password': $("#password").val()
                    	},
                    	success: function(res){
                    		if(res.success){
                    			window.location.href='/index.atc';
                    		} else {
                    			$('.alert-danger span').html(res.msg);
                    			$('.alert-danger', $('.login-form')).show();
                    		}
                    	}
                    });
	            }
	        });

	        $('.login-form input').keypress(function (e) {
	            if (e.which == 13) {
	                if ($('.login-form').validate().form()) {
	                    $.ajax({
	                    	url:"/userLogin.atc",
	                    	data:{
	                    		'sysUser.userName': $("#username").val(),
	                    		'sysUser.password': $("#password").val()
	                    	},
	                    	success: function(res){
	                    		if(res.success){
	                    			window.location.href='/index.atc';
	                    		} else {
	                    			$('.alert-danger span').html(res.msg);
	                    			$('.alert-danger', $('.login-form')).show();
	                    		}
	                    	}
	                    });
	                }
	                return false;
	            }
	        });
	}

    
    return {
        //main function to initiate the module
        init: function () {
        	
            handleLogin();     
	       
	       	$.backstretch([
		        "resources/img/bg/1.jpg",
		        "resources/img/bg/2.jpg",
		        "resources/img/bg/3.jpg",
		        "resources/img/bg/4.jpg"
		        ], {
		          fade: 1000,
		          duration: 8000
		    });
        }

    };

}();