/**
 * Created by lenovo on 2015/10/29.
 */

$(document).ready(function () {
    //切换

    var $div_li = $('div.tabs ul li');
    $div_li.click(function(){
        /*
        $(this).removeClass('tab1').removeClass('tab2')
            .addClass('tab1').siblings()
            .removeClass('tab1').addClass('tab2');
        */

        var index = $div_li.index(this);
        $('div.contents div')
            .eq(index).show()
            .siblings().hide()

    });

    $('li.tab2').click(function(){
        $(this).css({'color': '#1b8cf2', 'border-bottom': '2px #1b8cf2 solid', 'padding-bottom':'14px'})
            .siblings().remove('style').css({'color':'#999', 'border-bottom-width':'0'})

    });
    $('li.tab1').click(function(){
        $(this).css({'color': '#1b8cf2', 'border-bottom': '2px #1b8cf2 solid'})
            .siblings().remove('style').css({'color':'#999', 'border-bottom-width':'0', 'padding-bottom':'14px'})
    });


   

    

});








