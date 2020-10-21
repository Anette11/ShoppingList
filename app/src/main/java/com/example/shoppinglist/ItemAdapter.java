package com.example.shoppinglist;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ItemAdapter extends ListAdapter<Item, ItemAdapter.ItemAdaptorHolder> {

    private OnItemClickListenerInterface onItemClickListenerInterface;

    public interface OnItemClickListenerInterface {
        void onItemClickListenerInterfaceMethodEditItem(int position);

        void onItemClickListenerInterfaceMethodDeleteItem(int position);
    }

    public void setOnItemClickListenerInterface(OnItemClickListenerInterface onItemClickListenerInterface) {
        this.onItemClickListenerInterface = onItemClickListenerInterface;
    }

    protected ItemAdapter() {
        super(DIFF_UTIL_ITEM_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Item> DIFF_UTIL_ITEM_CALLBACK
            = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getItemType().equals(newItem.getItemType())
                    && oldItem.getItemAmount().equals(newItem.getItemAmount());
        }
    };

    @NonNull
    @Override
    public ItemAdaptorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemAdaptorHolder(itemView, onItemClickListenerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemAdaptorHolder holder, final int position) {
        final Item item = getItem(position);
        holder.textViewType.setText(item.getItemType());
        holder.textViewAmount.setText(item.getItemAmount());
    }

    static class ItemAdaptorHolder extends RecyclerView.ViewHolder {
        private final TextView textViewType;
        private final TextView textViewAmount;

        public ItemAdaptorHolder(@NonNull View itemView, final OnItemClickListenerInterface onItemClickListenerInterface) {
            super(itemView);
            textViewType = itemView.findViewById(R.id.text_view_item_type);
            textViewAmount = itemView.findViewById(R.id.text_view_amount);
            ImageView imageViewMenu = itemView.findViewById(R.id.imageView_menu);

            imageViewMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v, Gravity.END);
                    popupMenu.inflate(R.menu.menu_popup);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.menu_popup_edit) {
                                if (onItemClickListenerInterface != null) {
                                    int position = getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        onItemClickListenerInterface
                                                .onItemClickListenerInterfaceMethodEditItem(position);
                                    }
                                }
                                return true;
                            } else if (item.getItemId() == R.id.menu_popup_delete) {
                                if (onItemClickListenerInterface != null) {
                                    int position = getAdapterPosition();
                                    if (position != RecyclerView.NO_POSITION) {
                                        onItemClickListenerInterface
                                                .onItemClickListenerInterfaceMethodDeleteItem(position);
                                    }
                                }
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    showIconsInPopupMenu(popupMenu);
                    popupMenu.show();
                }
            });
        }
    }

    private static void showIconsInPopupMenu(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    assert menuPopupHelper != null;
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
        }
    }

    public Item getItemAt(int position) {
        return getItem(position);
    }
}
