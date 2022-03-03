// Id= "localhost"
Id= "101.200.61.174"
const Url = "http://"+Id+":9999/file/"
$(function () {
    //获取参数信息
    let href = window.location.href;
    // console.log(href)
    let url = href.substring(href.lastIndexOf("?"));
    let id = url.split(":")
    // console.log(id)


    //显示opj相关数据
    if (id[0] === "?opj") {
        //获取图片个数
        $.get(Url + "csv/number/" + id[1], "", function (number) {
            //显示图片
            // console.log(number)
            let $div = $("#div-image");
            for (let i = 0; i < number; i++) {
                $("<img src='"+Url+"/csv/picture/"+id[1]+"/"+i+"' alt='}'>").appendTo($div);
            }
        })
        //显示csv
        $.get(Url+"csv/text/"+id[1], "", function (data) {
            // console.log(data)
            showCsv(data)
        });

    } else if (id[0] === "?picture") {
        //显示图片
        let path = Url + "picture/" + id[1]
        $("#div-image").html(`<img src=${path}  alt=" ">`);
    } else {
        //显示csv
        $.get(Url + "csv/" + id[0], "", function (data) {
            showCsv(data)
        })
    }
})

function showCsv(txt) {


//wb.SheetNames[0]是获取Sheets中第一个Sheet的名字
//wb.Sheets[Sheet名]获取第一个Sheet的数据
    let strings = txt.split("\n");
    console.log(strings);
    let keyAry = strings[0].split(",")
    console.log(keyAry);
    let data = [];
    for (let i = 1; i < strings.length; i++) {
        data[i - 1] = strings[i].split(",")
    }
    console.log(data)
// 清除上次渲染的表格
    $("#demo").empty();
// 设置表格头
    $(`<thead><tr><th colspan=${keyAry.length}>${keyAry[0]}</th></tr></thead>`).appendTo($("#demo"));
    for (let d of data) {
        console.log(d);
        // 通过循环,每有一条数据添加一行表格
        let tr = $("<tr></tr>");
        for (let n = 0; n < keyAry.length; n++) {
            // 根据keyAry数组的长度,创建每一行表格中的td
            $("<td></td>").addClass(keyAry[n]).appendTo(tr);
        }
        // 遍历对象,根据键名找到是哪一列的数据,给对应的td添加内容

        for (let k in d) {

            // (tr[0].children[keyAry.indexOf(k)])

            $(tr[0].children[k]).html(d[k]);
        }

        tr.appendTo($("#demo"));

    }
}