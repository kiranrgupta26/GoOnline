var http = require('http');
var url = require('url');
var fs = require('fs');
var express = require('express');
var mysql = require('mysql');
var nodemailer = require('nodemailer');
var app = express();
var port = 8060;


var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "password",
  database:"data"
});
var query;

var letstry;
var jsonObj;
var table_name;

con.connect(function(err){
if(!err) {
    console.log("Connected To DataBase");    
} else {
    console.log("Error connecting database");    
}
});

var smtpTransport = nodemailer.createTransport({
   service:'Gmail',
  
   auth:{
       user:'kiran.rgupta26@gmail.com',
       pass:'password'
   },
   tls:{ rejectUnauthorized: false}
});

var rand,mailOptions,host,link,toEmail,toPassword;

app.get('/send',function(req,res)
{
    toEmail = req.param('to');
    toPassword = req.param('password');
    console.log('to Email '+toEmail);
    rand = Math.floor((Math.random()*100)+54);
    host = req.get('host');
    link = 'http://'+req.get('host')+'/verify?id='+rand;
	console.log('Link is '+link);
    mailOptions = {
        to : toEmail,
        subject : "Please Confirm Your Email Account",
        html : "Hello,<br>Please Click on the Link to verify your Email Account<br><a href="+link+">Click Here To Verify</a>"
    };
    console.log(mailOptions);
    smtpTransport.sendMail(mailOptions,function(error,response)
    {
        if(error)
        {
            console.log(error);
            res.send(404,"error");
        }
        else
        {
            console.log("Message Sent "+response.message);
            res.send(200,"Mail Sent");
        }
    });
});
var sendmyorders;
var allorders=[];
var count;
var myorderscount;

app.get('/getorders',function(req,res)
{ 
	var username = req.query.username;
	var myordersquery = "Select * from placedorders where username = ?"; 
   console.log('Inside Listorders');
	con.query(myordersquery,[username],function(err,result)
	{     
		if(err)
		{
			throw err;
		}
		else
		{             
            allorders = [];
            count = 0;
            myorderscount = result.length;
            
            for(var i=0;i<result.length;i++)
            {
                var orderid = result[i].orderid;
                console.log('Order id is '+orderid);
                
                getMyOrders(orderid,()=>{
                     count = count+1;
                    if(count==myorderscount)
                    {
                        sendmyorders = {myorders:allorders};
                        console.log('My Complete Orders '+JSON.stringify(sendmyorders));
                       // res.send(200,JSON.stringify(sendmyorders));
                        res.status(200).send(JSON.stringify(sendmyorders));
                    }
                });
            }
            
		}
	});
     
});

app.get('/cancelorder',function(req,res)
{
    var username = req.query.username;
    var orderid = req.query.orderid;
    
    var cancelorder = 'Delete from placedorders where username  =? and orderid = ?';
    con.query(cancelorder,[username,orderid],function(err,results)
    {
        if(err)
        {
            throw err;
        }
        else
        {
            if(results.affectedRows>0)
            {
                    console.log('Orders deleted');  
                    res.send(200,"OK");
            }
            else{
                console.log('Orders is already deleted');  
                res.send(200,"OK");
            }
           
        }
    });
});



app.get('/purchase',function(req,res)
{
    var purchase = [];
	var itemsList = [];
    var purchaseList = req.param('purchaselist');
    console.log('Purchase List ' +purchaseList);
    purchase = JSON.parse(purchaseList); 
    
    //Generate a Unique orderID
    var randid = Math.floor((Math.random()*1000)+8089);
    var orderid = Math.floor((Math.random()*1000)+randid)+Math.floor((Math.random()*3000));
    
    var itemdetails=[];
    itemdetails.push(orderid);
    itemdetails.push(purchase[0].username);
    var orderdate = new Date();
    itemdetails.push(orderdate);
   
	itemsList.push(itemdetails);
    var queryplaceorder = 'Insert into placedorders (orderid,username,orderdate) values ?';
    con.query(queryplaceorder,[itemsList],function(err,results)
             {
            if(err)
            {
                console.log('Error at puchase '+ err);
            }
            else{
                console.log('Affected rows id '+results.affectedRows);
            }
    });
    
    for(var i=0;i<purchase.length;i++)
    {
        var item_price;
        var item = purchase[i];
        
	    getDetailsAndPlacedOrder(item,orderid); 
        
    }
 
    res.send(200,"OK");
});


app.get('/checkCredential',function(req,res)
{
    var loginusername = req.param('username');
    var loginpassword = req.param('password');
    query = "Select * from userinfo where username =? AND password = ?";
    con.query(query,[loginusername,loginpassword],function(err,results)
    {
        if(err)
        {
            throw err;
        }
        else
        {
            console.log('No. of rows affected LoginRows '+results.length);
            if(results.length==1)
            {
                res.send(200,"Valid Credential");
            }
            else
            {
                res.send(400,"INValid Credential");
            }
        }
    });

});

app.get('/address',function(req,res)
{
	var useraddressdetails = {};
	var addressObj;
	var getuseraddress = req.param('username');	
	var useraddressquery = 'Select * from userinfo where username = ?';
	con.query(useraddressquery,[getuseraddress],function(err,results)
	{
		if(err)
		{
			throw err;
		}
		else
		{
			if(results.length!=0)
            {
                useraddressdetails = {fullname:results[0].fullname,address:results[0].address,address2:results[0].address2,landmark:results[0].landmark,pincode:results[0].pincode,mobileno:results[0].mobileno};
                addressObj = {useraddress:useraddressdetails};
                res.send(200,JSON.stringify(useraddressdetails));
            }
            else
            {
                   res.send(400,"No Address"); 
            }
			
		}
	});
});

app.get('/verify',function(req,res)
{
    console.log(req.protocol+"://"+req.get('host'));
    //if((req.protocal+"://"+req.get('host'))===("http://"+host))
    //{
        console.log('Domain Matched');
        console.log('request query id'+req.query.id);
        console.log('rand is'+rand);
        if(req.query.id==rand)
        {
            console.log('Email is Verified');
            query = "Insert into userinfo(username,password) values ?"; 
            var values = [[toEmail,toPassword]];
            con.query(query,[values],function(err,results)
            {
                if(err)
                {
                    throw err;
                }
                else
                {
                    console.log('No. of rows affected '+results.affectedRows);
                }
            });
            res.send(200,"<h1>Email "+mailOptions.to+" is been Successfully Verified</h1>");
        }
        else
        {
            console.log('Email is Not Verified');
            res.send(404,"<h1>BAD Request</h1>");
        }
    //}
   /* else
    {
        res.end("<h1>Request From Unknow Source</h1>");
    }*/
});

app.get('/userinfo',function(req,res)
{
	var userinfo = [];
	userinfo =JSON.parse(req.param('userinfo'));
	var info = userinfo[0];
	
	var insertuserinfo = 'Update userinfo set address = ? ,fullname = ?,address2 = ?,landmark = ?,pincode =? , mobileno = ? where username = ?';
	con.query(insertuserinfo,[info.address,info.fullname,info.address2,info.landmark,info.pincode,info.mobile,info.username],function(err,results)
	{
		if(err)
		{
			throw err;
		}
		else
		{
			res.send(200,'Details Updated');
		}
	});
});

app.get('/search',function(req,res)
{
   var category = req.param('category');
   var subcat1 = req.param('subcategory1');
   var subcat2 = req.param('subcategory2');
   var result1;

   table_name = 'textbooks';
   if(category==='Stationary')
   {
	table_name = 'stationary';
   }
   else if(category==='Note Books')
   {
	table_name = 'notebooks';
   }
   else if(category==='Text Books')
   {
	table_name = 'textbooks';
   }
   else if(category==='Guide')
   {
	table_name = 'guide';
   }
   else if(category==='Sample Papers')
   {
	table_name = 'samplepapers';
   }

   if(subcat1==='default' && subcat2==='default')
   {
	query = 'Select * from '+table_name;
   }
   else if(subcat2==='default')
   {
	query = 'Select * from '+table_name+' where subcat1 = '+'"'+subcat1+'"';
	console.log('Query is '+query);
   }
   else
   {
	query = 'Select * from '+table_name+' where subcat1 = ' + '"' + subcat1 + '"' + ' and subcat2 = '+ '"' +subcat2+'"';
	console.log('Query is '+query);
   }

       con.query(query,function(err,result,fields)
       {
           if(err)throw err;  

           var jsonData = {};
           var jsonArray=[];
           for(var i=0;i<result.length;i++)
           {
               var data = result[i].name;
               jsonData = {name:result[i].name,price:result[i].price.toString(),image_path:result[i].image_path,discount:result[i].discount.toString(),category:result[i].category,subcat1:result[i].subcat1,subcat2:result[i].subcat2};
               jsonArray.push(jsonData);
              console.log('JSON Data Send '+JSON.stringify(jsonData));
           }
           letstry = JSON.stringify(jsonArray);
            jsonObj = {temp:jsonArray};
           res.send(200,JSON.stringify(jsonObj));
       });
});

function getDetailsAndPlacedOrder(p_item,p_orderid)
{
	var itemList = [];
    var orderid = p_orderid;
	var getitem = p_item;
	var item_name = getitem.itemname;
	var query = 'Select * from '+getitem.sendcategory+' where name = ? and  subcat1 = ? and subcat2 = ?';
	var getquery = 'Select * from textbooks where name = ?';
	con.query(query,[item_name,getitem.sendsubcat1,getitem.sendsubcat2],function(err,results)
	{
		if(err)
		{
			console.log('Error at getdetailsand placed orders');
		}
		else
		{
			var getprice = results[0].price;
            var insertorderdetails = 'Insert into orderdetails (orderid,itemname,price,quantity,totalcost,category,subcat1,subcat2) values ?';
			var item1 = [];
            item1.push(orderid);		
			item1.push(getitem.itemname);
			item1.push(getprice);
			item1.push(getitem.itemquantity);
			item1.push(getprice*getitem.itemquantity);
			item1.push(getitem.sendcategory);
			item1.push(getitem.sendsubcat1);
			item1.push(getitem.sendsubcat2);
			itemList.push(item1);
			con.query(insertorderdetails,[itemList],function(err,result)
			{
				if(err)
				{
					console.log('Error inside else of function getdetails');
				}
				else
				{                   
					console.log('No of Rows Inserted '+result.affectedRows);
				}
			});
		}
	});
}

function getMyOrders(p_orderid,callback)
{   
    var orderid = p_orderid;
    var getordersdetail = "Select * from orderdetails where orderid = ?";  
               
            con.query(getordersdetail,[orderid],function(err,results)
            {
                var myorder = {};
                var ordersArray = [];
                if(err)
                {
                       throw err;
                }
                else
                 {
                    for(var j=0;j<results.length;j++)
                    {
                          myorder ={orderid:results[j].orderid,itemname:results[j].itemname,price:results[j].price.toString(),quantity:results[j].quantity.toString(),totalcost:results[j].totalcost.toString()};
                         ordersArray.push(myorder); 
                         
                    }               
                    allorders.push(ordersArray);                     
                    callback();
                }
                    
            });
}

app.listen(port,function()
{
   console.log('Server running'); 
});

