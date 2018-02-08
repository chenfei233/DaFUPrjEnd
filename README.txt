1、运行方式: java -jar DConverter.jar input/**
2、**目前支持的文件类型：
   1）.pdf
   2) .ppt/.pptx
   3) .doc/.docx
   4) .html/url
3、结果会根据类型自动保存。



该项目是根据DaFuPrj  和  poi  和  QPDF项目整合的

项目框架骨干是DaFuPrj+poi整合项目，只是在main()方法中进行了判断并添加了QPDF的调用而已。在pdf转html函数中，调用了html转md
现在运行的是大富项目，只要把大富项目方法换成另一个人就是poi项目了

