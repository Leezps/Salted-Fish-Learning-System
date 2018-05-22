package com.android.leezp.learncarproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.leezp.learncarproject.R;
import com.android.leezp.learncarproject.entity.PaymentTypeConstantEntity;
import com.android.leezp.learncarproject.entity.PaymentWayEntity;
import com.android.leezp.learncarproject.utils.listener.OnItemClickListener;
import com.android.leezp.learncarproject.utils.listener.OnItemLongClickListener;

import java.util.List;

/**
 * Created by Leezp on 2018/4/3.
 */

public class PaymentWayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //添加支付方式的viewType
    private final int addViewType = 1;
    //分割添加与方式的viewType
    private final int splitViewType = 2;
    //支付方式的viewtype
    private final int wayViewType = 3;
    //最开始RecyclerView的大小
    private final int initSize = 2;
    //支付方式的数据
    private List<PaymentWayEntity> data;
    //子控件的点击事件
    private OnItemClickListener itemClickListener;
    //子控件的长按点击事件
    private OnItemLongClickListener itemLongClickListener;

    public PaymentWayAdapter(List<PaymentWayEntity> data) {
        this.data = data;
    }

    /**
     * 删除一个数据
     * @param entity
     */
    public void removeDataChange(PaymentWayEntity entity) {
        if (data == null) return;
        data.remove(entity);
        notifyDataSetChanged();
    }

    /**
     * 网络请求到数据后整体改变
     * @param data
     */
    public void notifyDataChange(List<PaymentWayEntity> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    /**
     * 给RecyclerView设置子控件点击事件
     * @param itemClickListener
     */
    public void addOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 给RecyclerView设置子控件长按点击事件
     * @param itemLongClickListener
     */
    public void addOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    /**
     * 根据position的不同位置返回不同的ViewType
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return addViewType;
        } else if (position == 1) {
            return splitViewType;
        } else if (position - 2 < data.size()) {
            return wayViewType;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view;
        switch (viewType) {
            case addViewType:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_payment_way_item_add, parent, false);
                viewHolder = new AddViewHolder(view);
                break;
            case splitViewType:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_payment_way_item_split, parent, false);
                viewHolder = new SplitViewHolder(view);
                break;
            case wayViewType:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_payment_way_item_way, parent, false);
                viewHolder = new WayViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof WayViewHolder) {
            PaymentWayEntity entity = data.get(position - 2);
            WayViewHolder viewHolder = (WayViewHolder) holder;
            switch (entity.getPaymentType()) {
                case PaymentTypeConstantEntity.ALIPAY:
                    viewHolder.image.setImageResource(R.drawable.personal_payment_way_alipay);
                    viewHolder.content.setText(PaymentTypeConstantEntity.ALIPAY_STR);
                    break;
                case PaymentTypeConstantEntity.WECHAT:
                    viewHolder.image.setImageResource(R.drawable.personal_payment_way_wechat);
                    viewHolder.content.setText(PaymentTypeConstantEntity.WECHAT_STR);
                    break;
                case PaymentTypeConstantEntity.BUILD:
                    viewHolder.image.setImageResource(R.drawable.personal_payment_way_build);
                    viewHolder.content.setText(PaymentTypeConstantEntity.BUILD_STR);
                    break;
                case PaymentTypeConstantEntity.AGRICULTURE:
                    viewHolder.image.setImageResource(R.drawable.personal_payment_way_agriculture);
                    viewHolder.content.setText(PaymentTypeConstantEntity.AGRICULTURE_STR);
                    break;
                case PaymentTypeConstantEntity.BUSINESS:
                    viewHolder.image.setImageResource(R.drawable.personal_payment_way_business);
                    viewHolder.content.setText(PaymentTypeConstantEntity.BUSINESS_STR);
                    break;
            }
            if (entity.isShowRemove()) {
                viewHolder.remove.setVisibility(View.VISIBLE);
            } else {
                viewHolder.remove.setVisibility(View.GONE);
            }
            viewHolder.view.setTag(entity);
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, position);
                }
            });

            viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemLongClickListener.onItemLongClick(view);
                    return true;
                }
            });

            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, position);
                }
            });
        } else if (holder instanceof AddViewHolder) {
            ((AddViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? initSize : data.size() + initSize;
    }

    /**
     * 添加支付方式的ViewHolder
     */
    class AddViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public AddViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

    }

    /**
     * 分割添加与方式的ViewHolder
     */
    static class SplitViewHolder extends RecyclerView.ViewHolder {

        public SplitViewHolder(View itemView) {
            super(itemView);
        }

    }

    /**
     * 方式的ViewHolder
     */
    class WayViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView image;
        private TextView content;
        private TextView remove;

        public WayViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            image = ((ImageView) view.findViewById(R.id.fragment_payment_way_item_way_image));
            content = ((TextView) view.findViewById(R.id.fragment_payment_way_item_way_content));
            remove = ((TextView) view.findViewById(R.id.fragment_payment_way_item_way_remove));
        }
    }
}
