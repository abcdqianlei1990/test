package cn.xzj.agent.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xzj.agent.R;
import cn.xzj.agent.entity.job.JobFeature;
import cn.xzj.agent.ui.adapter.common.CommonViewHolder;
import cn.xzj.agent.widget.TagCloudLayout;

/**
 * @ Author MarkYe
 * @ Email yrmao9893@163.com
 * @ Date 2018/9/18
 * @ Des
 */
public class JobFeatureAdapter extends BaseExpandableListAdapter {
    private List<JobFeature> data;
    private Context mContext;
    private List<JobFeatureItemAdapter> mJobFeatureItemAdapters;
    private Map<Integer, String> mSelectedFeatureMap;
    private OnSelectedFeatureListener mOnSelectedFeatureListener;


    public JobFeatureAdapter(Context context, List<JobFeature> data) {
        this.data = data;
        this.mContext = context;
        this.mJobFeatureItemAdapters = new ArrayList<>();
        this.mSelectedFeatureMap = new HashMap<>();
        if (data == null)
            this.data = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        CommonViewHolder groupViewHolder;
        if (view == null) {
            groupViewHolder = new CommonViewHolder(mContext, LayoutInflater.from(mContext).inflate(R.layout.item_job_feature, null));
            view = groupViewHolder.itemView;
            view.setTag(groupViewHolder);
        } else {
            groupViewHolder = (CommonViewHolder) view.getTag();
        }
        groupViewHolder.setText(R.id.tv_group, data.get(i).getName());
        if (i == 0) {
            groupViewHolder.getView(R.id.view_line).setVisibility(View.GONE);
        } else {
            groupViewHolder.getView(R.id.view_line).setVisibility(View.VISIBLE);
        }
        if (b) {
            ((ImageView) groupViewHolder.getView(R.id.iv_group_icon)).setImageResource(R.mipmap.ic_arrow_up);
        } else {
            ((ImageView) groupViewHolder.getView(R.id.iv_group_icon)).setImageResource(R.mipmap.ic_arrow_down);
        }
        if (mJobFeatureItemAdapters.size() == 0) {
            JobFeatureItemAdapter mJobFeatureItemAdapter;
            for (JobFeature jobFeature : data) {
                mJobFeatureItemAdapter = new JobFeatureItemAdapter(mContext);
                mJobFeatureItemAdapter.addAllItem(jobFeature.getFeatures());
                mJobFeatureItemAdapters.add(mJobFeatureItemAdapter);
            }

        }
        return groupViewHolder.itemView;
    }

    @Override
    public View getChildView(final int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        CommonViewHolder childViewHolder;
        if (view == null) {
            childViewHolder = new CommonViewHolder(mContext, LayoutInflater.from(mContext).inflate(R.layout.item_job_feature_child, null));
            view = childViewHolder.itemView;
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (CommonViewHolder) view.getTag();
        }
        TagCloudLayout itemTagCloudLayout = childViewHolder.getView(R.id.tcl_feature);
        itemTagCloudLayout.setAdapter(mJobFeatureItemAdapters.get(i));
        itemTagCloudLayout.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                if (mJobFeatureItemAdapters.get(i).getSelectedPosition() == position) {
                    mJobFeatureItemAdapters.get(i).setSelectedPosition(-1);
                    mSelectedFeatureMap.remove(i);
                } else {
                    mJobFeatureItemAdapters.get(i).setSelectedPosition(position);
                    mSelectedFeatureMap.put(i, data.get(i).getFeatures().get(position).getId());
                }
                if (mOnSelectedFeatureListener != null)
                    mOnSelectedFeatureListener.getFeature(getSelectedFeature());
            }
        });
        return childViewHolder.itemView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private ArrayList<String> getSelectedFeature() {
        ArrayList<String> mFeatures = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : mSelectedFeatureMap.entrySet()) {
            mFeatures.add(entry.getValue());
        }
        return mFeatures;
    }

    public void removeSelectedFeature() {
        for (JobFeatureItemAdapter mJobFeatureItemAdapter : mJobFeatureItemAdapters) {
            mJobFeatureItemAdapter.setSelectedPosition(-1);
        }
        mSelectedFeatureMap.clear();
        if (mOnSelectedFeatureListener != null) {
            mOnSelectedFeatureListener.getFeature(getSelectedFeature());
        }

    }

    public void setOnSelectedFeatureListener(OnSelectedFeatureListener onSelectedFeatureListener) {
        this.mOnSelectedFeatureListener = onSelectedFeatureListener;
    }

   public interface OnSelectedFeatureListener {
        void getFeature(ArrayList<String> features);
    }
}
