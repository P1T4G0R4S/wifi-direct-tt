package com.ipn.tt.homescreen.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ipn.tt.homescreen.R;
import com.ipn.tt.homescreen.db.DBManager;
import com.ipn.tt.homescreen.db.User;

import java.util.List;

/**
 * Created by Iguanna on 02/07/2017.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.GridItemViewHolder> {
    private Context context;
    private List<Detail> mItemList;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    /**
     * Called when a grid adapter has been called
     *
     * @param context   The context of main activity
     * @param mItemList Detail List of recyclerview grid that contains data
     */
    public RecycleAdapter(Context context, List<Detail> mItemList) {
        this.context = context;
        this.mItemList = mItemList;
    }


    /**
     * Called when RecyclerView needs a new {@link GridItemViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(GridItemViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param position The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(GridItemViewHolder, int)
     */
    @Override
    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new GridItemViewHolder(itemView, this);
    }


    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link GridItemViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>

     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link GridItemViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(GridItemViewHolder, int)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(GridItemViewHolder holder, int position) {
        final Detail items = mItemList.get(position);
        holder.mTitle.setText(items.getName());
        holder.btnInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "PRUEBAS BTNINFO" + items.id , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), ContactDetails.class);
                intent.putExtra("id", String.valueOf(items.id));
                context.startActivity(intent);
            }
        });
        holder.btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "PRUEBAS BTNSearch", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), ContactSearch.class);
                //Intent intent = new Intent(getApplicationContext(), ContactSearch.class);
                context.startActivity(intent);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                //Toast.makeText(v.getContext(), "PRUEBAS BTNDELETE", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("¿Desea eliminar contacto?");
                        builder.setMessage("Este contacto será eliminado permanentemente de la aplicación");
                        builder.setNegativeButton("CANCELAR",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //Toast.makeText(v.getContext(),"No is clicked",Toast.LENGTH_LONG).show();
                                    }
                                });
                        builder.setPositiveButton("ELIMINAR",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //Toast.makeText(v.getContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                                        DBManager db = new DBManager(v.getContext());
                                        db.open();
                                        User usr = new User();
                                        usr.id_user = items.id;
                                        db.delete(usr);
                                        db.close();

                                        Intent i = new Intent(v.getContext(),MainActivity.class);
                                        v.getContext().startActivity(i);
                                        ((Activity)v.getContext()).finish();

                                    }
                                });
                        builder.show();
            }
        });
        //holder.mPosition.setText("" + items.getPosition());
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     * Called when a grid item has been called
     *
     * @param onItemClickListener The view that was clicked.
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(GridItemViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }


    public class GridItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public RecycleAdapter mAdapter;
        public Button btnInfo;
        public Button btnSearch;
        public Button btnDelete;
        public GridItemViewHolder(View itemView, RecycleAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            //mPosition = (TextView) itemView.findViewById(R.id.item_position);
            itemView.setOnClickListener(this);
            this.btnInfo = (Button) itemView.findViewById(R.id.btn_infoContact);
            this.btnSearch = (Button) itemView.findViewById(R.id.btn_searchContact);
            this.btnDelete = (Button) itemView.findViewById(R.id.btn_deleteContact);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}