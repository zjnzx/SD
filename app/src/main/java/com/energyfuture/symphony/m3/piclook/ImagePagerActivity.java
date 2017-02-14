package com.energyfuture.symphony.m3.piclook;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.common.GetPicFromWifiSDScard;
import com.energyfuture.symphony.m3.dao.TrDetectionTempletFileObjDao;
import com.energyfuture.symphony.m3.dao.TrSpecialBushingFileDao;
import com.energyfuture.symphony.m3.domain.TrDetectiontempletFileObj;
import com.energyfuture.symphony.m3.domain.TrSpecialBushingFile;
import com.energyfuture.symphony.m3.domain.TrTask;
import com.energyfuture.symphony.m3.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片查看器
 */
public class ImagePagerActivity extends FragmentActivity {
    private Context context = ImagePagerActivity.this;
	private static final String STATE_POSITION = "STATE_POSITION";
	private HackyViewPager mPager;
	private int pagerPosition;
    private String picUrl,id,localurl,type,filenumber="",mainid;
    private ArrayList<String> ids = new ArrayList<String>();
	private TextView indicator,picturenumber;
    private ArrayList<String> picNameList =  new ArrayList<String>();
    private ArrayList<String> picNumberlist =  new ArrayList<String>();
    private ArrayList<String> picIdList =  new ArrayList<String>();
    private ImageView getimage;
    private LinearLayout image_back;
    private ArrayList<String>  urlList = new ArrayList<String>();
    private GetPicFromWifiSDScard getPicFromWifiSDScard;
    private TrTask trTask;
    private final String pathTemp = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private final String FILE_SAVEPATH = pathTemp.substring(0,
            pathTemp.length() - 1)
            + "legacy" + "/M3/transformer/picture/";
    private String pic_route;
    private ImagePagerAdapter mAdapter;
    private int position;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail_pager);

        initView();

        CharSequence text = getString(R.string.viewpager_indicator,  1, picNameList.size());
		indicator.setText(text);
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}
        for (int i = 0; i < picIdList.size(); i++) {
            if (i == pagerPosition) {
                mPager.setCurrentItem(i);
                if(picNumberlist.get(i) == null)
                    picturenumber.setText("无");
                else
                    picturenumber.setText(picNumberlist.get(i));
            }
        }
	}

    private void initView(){
        pagerPosition = getIntent().getIntExtra("index", 0);
        id=getIntent().getStringExtra("id");  //Detectionobjid,Positionid
        picUrl=getIntent().getStringExtra("url");
        localurl=getIntent().getStringExtra("localurl");
        type=getIntent().getStringExtra("type");

        if(type.equals("general")){//非红外普通图片
            filenumber=getIntent().getStringExtra("filenumber");
            queryTrDetectionTempletFileObj();
        }else if(type.equals("bushing")){//红外和非红外套管图片
            queryTrSpecialBushingFile();
        }else if(type.equals("infgeneral")){//红外普通图片
            queryInfFile();
        }if(type.equals("imageshow")){
            for (Map.Entry<String,String> entry : Constants.datamap.entrySet()){
                String number=entry.getKey();
                String name=entry.getValue();

                picNumberlist.add(number);
                picNameList.add(name);
            }
            urlList.addAll(picNameList);
            picIdList.addAll(picNameList);
        }

        image_back = (LinearLayout) findViewById(R.id.image_back);
        getimage= (ImageView) findViewById(R.id.getimage);
        mPager = (HackyViewPager) findViewById(R.id.pager);
        indicator = (TextView) findViewById(R.id.indicator);
        picturenumber = (TextView) findViewById(R.id.picturenumber);

        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urlList);
        mPager.setAdapter(mAdapter);

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.getImageHandler!=null) {
                    Constants.getImageHandler.sendEmptyMessage(3); //更新ImageGetActivity的页面
                }
                finish();
            }
        });

        if(type.equals("imageshow")){
            getimage.setVisibility(View.VISIBLE);
            getimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getimage.setVisibility(View.GONE);
                    String url =picUrl+ picNameList.get(position);
                    ImageDetailFragment.newInstance(url,localurl,picNameList.get(position));
                }
            });
        }

        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                CharSequence text = getString(R.string.viewpager_indicator, position + 1, picNameList.size());
                ImagePagerActivity.this.position=position;
                indicator.setText(text);
                if(picNumberlist.get(position) == null)
                    picturenumber.setText("无");
                else
                    picturenumber.setText(picNumberlist.get(position));
            }
        });

    }

    /**
     * 查询附件表
     */
    private void queryTrDetectionTempletFileObj(){
        TrDetectionTempletFileObjDao trDetectionTempletFileObjDao = new TrDetectionTempletFileObjDao(this);
        TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
        trDetectiontempletFileObj.setDetectionobjid(id);
        trDetectiontempletFileObj.setFilenumber(filenumber);
        //查询图片的信息
        List<TrDetectiontempletFileObj> fileList = trDetectionTempletFileObjDao.queryTrDetectionTempletFileObjList(trDetectiontempletFileObj);
        for(TrDetectiontempletFileObj file : fileList){
            urlList.add(file.getFileurl());
            picNameList.add(file.getFilename());
            picNumberlist.add(file.getFilenumber());
            picIdList.add(file.getId());
        }
    }
    /**
     * 查询附件表
     */
    private void queryTrSpecialBushingFile(){
        TrSpecialBushingFileDao trSpecialBushingFileDao = new TrSpecialBushingFileDao(this);
        TrSpecialBushingFile  trSpecialBushingFile = new TrSpecialBushingFile();
        trSpecialBushingFile.setPositionid(id);
        //查询图片的信息
        List<TrSpecialBushingFile> fileList = trSpecialBushingFileDao.queryTrSpecialBushingFileList(trSpecialBushingFile);
        if(fileList.size() > 0){
            for(TrSpecialBushingFile file : fileList) {
                urlList.add(file.getFileurl());
                picNameList.add(file.getFilename());
                picNumberlist.add(file.getFilenumber());
                picIdList.add(file.getId());
            }
        }
    }

    /**
     * 查询附件表
     */
    private void queryInfFile(){
        TrDetectionTempletFileObjDao trDetectionTempletFileObjDao = new TrDetectionTempletFileObjDao(this);
        TrDetectiontempletFileObj trDetectiontempletFileObj = new TrDetectiontempletFileObj();
        trDetectiontempletFileObj.setSignid(id);
        //查询图片的信息
        List<TrDetectiontempletFileObj> fileList = trDetectionTempletFileObjDao.queryTrDetectionTempletFileObjList(trDetectiontempletFileObj);
        for(TrDetectiontempletFileObj file : fileList){
            urlList.add(file.getFileurl());
            picNameList.add(file.getFilename());
            picNumberlist.add(file.getFilenumber());
            picIdList.add(file.getId());
        }
    }

    @Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ArrayList<String> fileList;

		public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url =picUrl+ picNameList.get(position);

			return ImageDetailFragment.newInstance(url,localurl,picNameList.get(position));
		}

	}
}
