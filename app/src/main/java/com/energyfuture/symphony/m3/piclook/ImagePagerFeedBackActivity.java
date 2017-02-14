package com.energyfuture.symphony.m3.piclook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.energyfuture.symphony.m3.activity.R;
import com.energyfuture.symphony.m3.dao.TrFeedBackAccessoryDao;
import com.energyfuture.symphony.m3.domain.TrFeedBackAccessory;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片查看器
 */
public class ImagePagerFeedBackActivity extends FragmentActivity {
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index"; 
	private HackyViewPager mPager;
	private int pagerPosition;
    private String picUrl,id,localurl;
	private TextView indicator,picturenumber;
    private ArrayList<String> picNameList =  new ArrayList<String>();
    private ArrayList<String> picIdList =  new ArrayList<String>();
    private LinearLayout image_back;
    private ArrayList<String>  urlList = new ArrayList<String>();

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
                picturenumber.setText(picNameList.get(i));
            }
        }
	}

    private void initView(){
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        picUrl=getIntent().getStringExtra("url");
        localurl=getIntent().getStringExtra("localurl");
        id=getIntent().getStringExtra("id");

        image_back = (LinearLayout)findViewById(R.id.image_back);
        mPager = (HackyViewPager) findViewById(R.id.pager);
        indicator = (TextView) findViewById(R.id.indicator);
        picturenumber = (TextView) findViewById(R.id.picturenumber);

        queryAccessory();

        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), urlList);
        mPager.setAdapter(mAdapter);


        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
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
                indicator.setText(text);
                picturenumber.setText(picNameList.get(position));
            }
        });

    }

    /**
     * 查询附件表
     */
    private void queryAccessory(){
        TrFeedBackAccessoryDao trFeedBackAccessoryDao = new TrFeedBackAccessoryDao(this);
        TrFeedBackAccessory trFeedBackAccessory = new TrFeedBackAccessory();
        trFeedBackAccessory.setReplyid(id);
        //查询图片的信息
        List<TrFeedBackAccessory>  accList = trFeedBackAccessoryDao.queryTrFeedBackAccessoryList(trFeedBackAccessory);
        for(TrFeedBackAccessory accessory : accList){
            urlList.add(accessory.getFileurl());
            picNameList.add(accessory.getFilename());
            picIdList.add(accessory.getId());
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
			String url = picUrl+ picNameList.get(position);

			return ImageDetailFragment.newInstance(url,localurl,picNameList.get(position));
		}

	}
}
