package com.xl.testui.iflytek;

import java.util.ArrayList;

public class SpeechContentProvider {

    public static ArrayList<String> msContent = new ArrayList<>();
    public static ArrayList<String> jdContent = new ArrayList<>();
    public static ArrayList<String> jddContent = new ArrayList<>();

    public static ArrayList<String> msContentSelect = new ArrayList<>();
    public static ArrayList<String> jdContentSelect = new ArrayList<>();
    public static ArrayList<String> jddContentSelect = new ArrayList<>();

    public static ArrayList<String> msContentDetail = new ArrayList<>();
    public static ArrayList<String> jdContentDetail = new ArrayList<>();
    public static ArrayList<String> jddContentDetail = new ArrayList<>();
    static {
//        按POI参考点检索周边的美食
        msContent.add("附近有什么好吃的");
        msContent.add("石头城附近有推荐的餐厅吗?");
        msContent.add("帮我在徐汇区美罗大厦边上找个吃饭的地方");
        msContent.add("帮我找个餐厅");
        msContent.add("我饿了");
//        按商圈/行政区来检索美食
        msContent.add("在水游城商场内给我找个中餐");
        msContent.add("帮我在新街口附近推荐一家吃烤肉的餐厅");
        msContent.add("德基广场有上海菜吗");
        msContent.add("我要去美罗城，帮我找个咖啡馆");
        msContent.add("玄武区附近吃火锅的地方");
        msContent.add("新城市广场有没有吃涮羊肉的地方");
        msContent.add("南京玄武区有吃龙虾的地方吗");
//         按关键词检索美食
        msContent.add("我想去吃小龙虾");
        msContent.add("附近有推荐吃烤全羊的地方吗");
        msContent.add("美罗城附近有素食店吗");
        msContent.add("附近的龙虾店");
        msContent.add("附近有卖驴肉火烧的店吗");
//        按一级品类检索美食
        msContent.add("带我去吃火锅");
        msContent.add("附近的快餐店");
        msContent.add("帮我找家奶茶店");
        msContent.add("我想找一家咖啡馆");
        msContent.add("附近有没有吃韩式烤肉的地方");
        msContent.add("有推荐的吃老北京火锅的地方吗");
        msContent.add("帮我推荐一个吃自助海鲜的餐厅");
//        按距离来检索美食
        msContent.add("附近2公里以内吃饭的地方");
        msContent.add("2.1公里以内的中餐厅");
        msContent.add("离这800米以内的咖啡厅");
        msContent.add("我想找个两公里内的蛋糕店");
//        按驾车时间来检索美食
        msContent.add("开车10min能到的人均价格200左右的餐厅");
        msContent.add("开车半小时以内的淮扬菜");
        msContent.add("有30min能到的重庆火锅店吗");
        msContent.add("附近有没有10min能到川菜馆");
//         按照人均价格检索
        msContent.add("附近找家人均200到300之间的自助烤肉店");
        msContent.add("帮我找找家价格200元以内的小火锅店");
        msContent.add("想吃涮羊肉，找家价格在100元到300元左右的火锅店");
//        按照POI+关键词检索美食
        msContent.add("帮我在新城市广场找个吃龙虾的地方");
        msContent.add("新城市广场有吃烤鱼的店吗");
        msContent.add("找个附近的喝咖啡的店");
//        按照POI+一级品类检索美食
        msContent.add("我想去吃小龙虾");
        msContent.add("附近有推荐吃烤全羊的地方吗");
        msContent.add("美罗城附近有素食店吗");
        msContent.add("附近的龙虾店");
        msContent.add("附近有卖驴肉火烧的店吗");
//        按照POI+距离/驾车时间检索美食
        msContent.add("帮我找南艺两公里以内的中餐馆");
        msContent.add("去找一汽NBD五公里以内的餐厅");
        msContent.add("长春华阳大饭店开车十分钟能到的快餐店");
//        按照POI+人均价格检索美食
        msContent.add("看看奥体大厦边上人均200左右的餐厅");
        msContent.add("推荐下奥体大厦附近价格不超过200元的西餐厅");
        msContent.add("看下有价格不超过100元奥体大厦周围的餐厅吗");
        msContent.add("新街口附近200元以内的餐厅");
//         按照POI+一级品类+关键词检索美食
        msContent.add("我想找家奥体大厦附近特色菜有椒麻鱼的中餐厅");
        msContent.add("帮忙找一下吃牛肉火锅店的火锅店");
        msContent.add("帮我推荐一家奥体大厦附近的吃海鲜的自助餐厅");
        msContent.add("上海美罗城有人均不超过300的吃海鲜自助的餐厅吗");
//        按照POI+一级品类+距离/驾车时间检索美食
        msContent.add("看看奥体大厦边上一公里以内人均200左右的餐厅");
        msContent.add("推荐下奥体大厦附近不超过两公里价格不超过200元的西餐厅");
        msContent.add("看下价格不超过100元奥体大厦周围开车10min能到的餐厅");
//        按照POI+一级品类+人均价格检索美食
        msContent.add("我想去吃小龙虾");
        msContent.add("附近有推荐吃烤全羊的地方吗");
        msContent.add("美罗城附近有素食店吗");
        msContent.add("附近的龙虾店");
        msContent.add("附近有卖驴肉火烧的店吗");
//         按照POI+距离/驾车时间+人均价格检索美食
        msContent.add("看看奥体大厦附近两公里以内人均200左右的西餐厅");
        msContent.add("推荐下奥体大厦附近开车30min能到价格不超过200元的意大利餐厅");
        msContent.add("看下价格不超过100元奥体大厦周围的两公里以内的重庆火锅 ");
//        按照POI+一级品类+距离/驾车时间+人均价格检索美食
        msContent.add("看看奥体大厦附近两公里以内人均200左右的西餐厅");
        msContent.add("推荐下奥体大厦附近开车30min能到价格不超过200元的意大利餐厅");
        msContent.add("看下价格不超过100元奥体大厦周围的两公里以内的重庆火锅");
//        按照商圈+距离检索美食
        msContent.add("水游城附近一公里以内的快餐店");
        msContent.add("新街口周围一公里的烤肉店");
//         按照商圈+关键词检索美食
        msContent.add("找一家新街口的自助餐厅");
        msContent.add("在新街口找家鸭血粉丝汤馆");
        msContent.add("在虹悦城找一下汉堡王");
        msContent.add("我想吃新街口附近的大排档");
        msContent.add("新城市广场有吃烧烤的地儿吗");
//        按照商圈+人均价格检索美食
        msContent.add("我需要找家价格150-200的日式餐厅");
        msContent.add(".新街口有没有价格在100元以内的吃南京菜的地方");
//        按照商圈+一级品类检索美食
        msContent.add("带我找家新街口附近的汉堡店");
        msContent.add("找个虹悦城的淮扬菜馆");
        msContent.add("夫子庙有烧烤店吗 ");


        msContentSelect.add("评价比较好的");
        msContentSelect.add("评价好");
        msContentSelect.add("评分不错的有吗");
        msContentSelect.add("评价不错的推荐下");
        msContentSelect.add("评分5分的");
        msContentSelect.add("评分4.7分以上的");
        msContentSelect.add("水游城商场内的");
        msContentSelect.add("美罗城附近的");
        msContentSelect.add("我要在德基广场的");
        msContentSelect.add("距离新街口1公里以内的");
        msContentSelect.add("有小龙虾吗");
        msContentSelect.add("我想吃涮羊肉");
        msContentSelect.add("找一下可以吃烤全羊的");
        msContentSelect.add("可以吃小龙虾的地方");
        msContentSelect.add("特色菜带酸菜鱼的");
        msContentSelect.add("有没有海鲜餐厅");
        msContentSelect.add("找家咖啡馆");
        msContentSelect.add("找个奶茶店");
        msContentSelect.add("要农家菜");
        msContentSelect.add("想吃火锅");
        msContentSelect.add("距离两公里以内的");
        msContentSelect.add("走路能到的");
        msContentSelect.add("一公里以内能到的");
        msContentSelect.add("距离不超过三公里的");
        msContentSelect.add("找家人均200到300之间的");
        msContentSelect.add("人均不超过300的有吗");
        msContentSelect.add("价格200元以内的");
        msContentSelect.add("100元到300元左右的");

        msContentSelect.add("导航去第2个");
        msContentSelect.add("我要去第一家");
        msContentSelect.add("打电话给第二家");
        msContentSelect.add("联系第一家");
        msContentSelect.add("默认排序");
        msContentSelect.add("离我最近的");
        msContentSelect.add("评分高到低排序");
        msContentSelect.add("价格从低到高");

        msContentDetail.add("帮我导航去这儿");
        msContentDetail.add("开始导航");
        msContentDetail.add("现在导航");
        msContentDetail.add("立即导航");
        msContentDetail.add("打电话给这家");
        msContentDetail.add("帮我打电话");
        msContentDetail.add("我要打电话去这儿");
        msContentDetail.add("帮我预定座位");
        msContentDetail.add("我想预定座位");

        jdContent.add("推荐点附近的景点玩玩");
        jdContent.add("我想看看附近有哪些景点");
        jdContent.add("我想看看有哪些景点推荐");
        jdContent.add("有没有什么景点值得去玩的");
        jdContent.add("上海有什么景点推荐");
        jdContent.add("南京有什么好玩的");
        jdContent.add("去山东应该去哪些景区");
        jdContent.add("哈尔滨有哪些景区可以去看看");
        jdContent.add("5公里范围内的景点推荐");
        jdContent.add("周围3公里左右，有什么景点推荐");
        jdContent.add("找一下10公里以内的景区");
        jdContent.add("开车20公里范围内的景点");
        jdContent.add("大蜀山怎么样");
        jdContent.add("帮我查一下迪士尼这个景点咋样");
        jdContent.add("我想了解下景点龙门石窟");

        jdContentSelect.add("我要价格50到100之间的");
        jdContentSelect.add("价格100元以内的");
        jdContentSelect.add("看看不低于200元的");
        jdContentSelect.add("三公里以内的");
        jdContentSelect.add("找五公里以内的");
        jdContentSelect.add("找找10min能到的");
        jdContentSelect.add("我要30min能到的");
        jdContentSelect.add("大蜀山那个");
        jdContentSelect.add("最近的那个");
        jdContentSelect.add("最远的那个");
        jdContentSelect.add("最贵的那个");
        jdContentSelect.add("最便宜的那个");

        jdContentSelect.add("导航去第2个");
        jdContentSelect.add("我要去第一家");
//        jdContentSelect.add("打电话给第二家");
//        jdContentSelect.add("联系第一家");
        jdContentDetail.add("导航过去");
        jdContentDetail.add("现在就带我过去吧");
        jdContentDetail.add("现在就导航过去");

        jddContent.add("在附近找一家桔子水晶精选酒店");
        jddContent.add("上海虹桥附近有全季酒店吗");
        jddContent.add("最近的诺富特酒店在哪儿");
        jddContent.add("找一下附近的希尔顿酒店");
        jddContent.add("周围有如家吗");
        jddContent.add("南艺周边有7天吗");
        jddContent.add("在这附近有没有汉庭");
        jddContent.add("我想找一家徐家汇附近的和颐酒店");
        jddContent.add("我要去上海徐家汇，有没有推荐的酒店");
        jddContent.add("看一下北京首都大酒店");
        jddContent.add("找一家徐汇区的酒店");
        jddContent.add("南京市鼓楼区有五星级的酒店吗");
        jddContent.add("给我在市中心找一个经济点儿的酒店");
        jddContent.add("美罗广场周围适合商务出行的酒店");
        jddContent.add("夫子庙附近有民宿吗");
        jddContent.add("我想看看附近300元到500元之间评价比较好的酒店");
        jddContent.add("附近有没有高端连锁酒店");
        jddContent.add("附近有酒店式公寓吗");
        jddContent.add("帮我找家民宿");
        jddContent.add("南京南站附近适合商务出行的酒店");
        jddContent.add("帮我看看美罗附近两公里以内的连锁酒店");
        jddContent.add("找找看夫子庙附近两公里以内评分在4.5以上的酒店");
        jddContent.add("推荐一家附近四星级以上的酒店吧");
        jddContent.add("附近两公里以内的商务酒店");
        jddContent.add("附近开车半小时能到的酒店");
        jddContent.add("有没有20分钟内能到中山陵景区附近的酒店");
        jddContent.add("苏州虎丘风景区附近有适合一家人住的酒店吗");
        jddContent.add("帮我看看美罗附近两公里以内的连锁酒店");
        jddContent.add("我想找一家靠近苏州拙政园的精品酒店");
        jddContent.add("帮我找个中山陵附近不超过 1500 元带游泳池和健身房的酒店");
        jddContent.add("帮我推荐个国展中心周边2000元以下带停车场游泳池的酒店");
        jddContent.add("我要找颐和园周边有泳池的不超过3000元的高端酒店");
        jddContent.add("附近评分4.5分以上价格400元左右的酒店");
        jddContent.add("找一下一汽NBD附10公里以内的价格500元以内的四星级酒店");

        jddContentSelect.add("我要价格200到300之间的");
        jddContentSelect.add("价格500元以内的");
        jddContentSelect.add("看看不低于300元的");
        jddContentSelect.add("我要四星级以上的");
        jddContentSelect.add("四星级酒店");
        jddContentSelect.add("找一下五星级的");
        jddContentSelect.add("找找档次高一点的");
        jddContentSelect.add("我想找快捷连锁酒店");
        jddContentSelect.add("有没有高端点的");
        jddContentSelect.add("三公里以内的");
        jddContentSelect.add("找五公里以内的");
        jddContentSelect.add("找找10min能到的");
        jddContentSelect.add("我要30min能到的");
        jddContentSelect.add("适合度假的");
        jddContentSelect.add("适合约会的");
        jddContentSelect.add("商务出行可以住的");
        jddContentSelect.add("出差住的");
        jddContentSelect.add("一家人住的");
        jddContentSelect.add("带免费停车场的");
        jddContentSelect.add("带24小时健身房的");

        jddContentSelect.add("导航去第2个");
        jddContentSelect.add("我要去第一家");
        jddContentSelect.add("打电话给第二家");
        jddContentSelect.add("联系第一家");
        jddContentSelect.add("价格便宜点的");
        jddContentSelect.add("评分高的");
        jddContentSelect.add("受欢迎的");
        jddContentSelect.add("离的近的");
        jddContentSelect.add("第一家是温泉酒店吗");
        jddContentSelect.add("第二家适合一家人入住吗");
        jddContentSelect.add(" 维也纳酒店适合情侣吗");
        jddContentSelect.add("第一家有早餐吗");
        jddContentSelect.add("这家酒店有游泳池吗");

        jddContentDetail.add("导航过去");
        jddContentDetail.add("现在就带我过去吧");
        jddContentDetail.add("现在就导航过去");
        jddContentDetail.add("打电话给这家");
        jddContentDetail.add("帮我打电话");
        jddContentDetail.add("打电话给这家酒店");
        jddContentDetail.add("是温泉酒店吗");
        jddContentDetail.add("适合一家人入住吗");
        jddContentDetail.add("适合情侣吗");
        jddContentDetail.add("有早餐吗");
        jddContentDetail.add("这家酒店有游泳池吗");

    }


}
