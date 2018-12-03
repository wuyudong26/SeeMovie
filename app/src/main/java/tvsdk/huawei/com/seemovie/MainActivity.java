package tvsdk.huawei.com.seemovie;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String[] list1={"21克拉",
                    "三块广告牌 原声版 新",
                    "水形物语 原声版",
                    "黑豹 原声版",
                    "恋爱回旋 原声版",
                    "降临 原声版",
                    "红海行动",
                    "神秘巨星 原声版",
                    "马戏之王 原声版",
                    "唐人街探案2",
                    "厉害了，我的国",
                    "祖宗十九代",
                    "小萝莉的猴神大叔 原声版",
                    "闺蜜2：无二不作",
                    "捉妖记2",
                    "西游记女儿国",
                    "冰川时代5：星际碰撞 原声版",
                    "前任3",
                    "至暗时刻 原声版",
                    "勇敢者游戏：决战丛林 原声版",
                    "雷神3：诸神黄昏 原声版",
                    "羞羞的铁拳",
                    "银河护卫队2 原声版",
                    "战狼2",
                    "无问西东"};
    String[] list2={"http://7xvl2z.com1.z0.glb.clouddn.com/nigg2.mp4",
                     "http://202.99.114.93:80/00000000/00000000000000000000000001348052/sankuaiguanggaopai.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001354915/shuixingwuyushanjianban.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001350023/heibao.ts",
                       "http://202.99.114.93:80/00000000/00000000000000000000000000699135/XOTQ4MTIxMzg0.ts",
                        "http://202.99.114.93:80/00000000/00000000000000000000000001339153/jianglin.ts",
                       "http://202.99.114.93:80/00000000/00000000000000000000000001333984/honghaixingdong.ts",
                        "http://202.99.114.93:80/00000000/00000000000000000000000001325945/shenmijuxingyuanshengban.ts",
                        "http://202.99.114.93:80/00000000/00000000000000000000000001324634/maxizhiwang.ts",
                        "http://202.99.114.93:80/00000000/00000000000000000000000001324572/tangrenjietanan2.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001322630/lihailewodeguo.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001320116/zuzongshijiudai.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001178882/xiaoluolidehoushendashu.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001319218/guimi2.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001317323/zhuoyaoji2.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001314828/xiyoujinverguo.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001305778/bcsdwxjpz.ts",
                    "http://202.99.114.93:80/00000000/Umai:MOVI/16925126@BESTV.SMG.SMG/267072.8000.ts",

                    "http://202.99.114.93:80/00000000/00000000000000000000000001280178/ygzyxjzcl.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001257038/leishen3yuanshengban.ts",
                            "http://202.99.114.93:80/00000000/Umai:MOVI/16175354@BESTV.SMG.SMG/263679.8000.ts",
                    "http://202.99.114.93:80/00000000/Umai:MOVI/14804726@BESTV.SMG.SMG/260263.8000.ts",
                            "http://202.99.114.93:80/00000000/00000000000000000000000001157851/zhanlang2.ts",
                    "http://202.99.114.93:80/00000000/00000000000000000000000001370733/wuwenxidong.ts",
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list=findViewById(R.id.list);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (this,R.layout.array_item,list1);
        list.setOnItemClickListener(this);
        list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(MainActivity.this, MediaplayerActivity.class);
        intent.putExtra("url",list2[position]);
        startActivity(intent);

    }
}
