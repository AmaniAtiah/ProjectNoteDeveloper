package com.barmej.notedeveloper2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.barmej.notedeveloper2.BR;
import com.barmej.notedeveloper2.Constants;
import com.barmej.notedeveloper2.R;
import com.barmej.notedeveloper2.data.entity.CheckNote;
import com.barmej.notedeveloper2.data.entity.Note;
import com.barmej.notedeveloper2.data.entity.PhotoNote;
import com.barmej.notedeveloper2.databinding.ItemNoteBinding;
import com.barmej.notedeveloper2.databinding.ItemNoteCheckBinding;
import com.barmej.notedeveloper2.databinding.ItemNotePhotoBinding;
import com.barmej.notedeveloper2.listener.ItemClickListenerCheckNote;
import com.barmej.notedeveloper2.listener.ItemClickListenerNote;
import com.barmej.notedeveloper2.listener.ItemLClickListenerPhotoNote;
import com.barmej.notedeveloper2.listener.ItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Note> mItems;
    Context context;
    private ItemClickListenerNote mItemClickListenerNote;
    private ItemLClickListenerPhotoNote mItemLClickListenerPhotoNote;
    private ItemClickListenerCheckNote mItemClickListenerCheckNote;
    private ItemLongClickListener mItemLongClickListener;

    public NoteAdapter(ArrayList<Note> mItems,Context context,ItemClickListenerNote mItemClickListenerNote,ItemLClickListenerPhotoNote mItemLClickListenerPhotoNote,ItemClickListenerCheckNote mItemClickListenerCheckNote,ItemLongClickListener mItemLongClickListener) {
        this.mItems = mItems;
        this.context = context;
        this.mItemClickListenerNote = mItemClickListenerNote;
        this.mItemLClickListenerPhotoNote = mItemLClickListenerPhotoNote;
        this.mItemClickListenerCheckNote = mItemClickListenerCheckNote;
        this.mItemLongClickListener = mItemLongClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof PhotoNote) {
            return Constants.IMAGE_DATA;
        }else if(mItems.get(position) instanceof CheckNote) {
            return Constants.CHECK_DATA;
        } else {
            return Constants.NOTE_DATA;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == Constants.NOTE_DATA) {
            ItemNoteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_note, parent, false);
            viewHolder =  new TextNoteViewHolder(binding, mItemClickListenerNote, mItemLongClickListener);
        } else if(viewType == Constants.IMAGE_DATA) {
            ItemNotePhotoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_note_photo, parent, false);
            viewHolder =  new ImageNoteViewHolder(binding, mItemLClickListenerPhotoNote, mItemLongClickListener);
        }else if(viewType == Constants.CHECK_DATA){
            ItemNoteCheckBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_note_check, parent, false);
            viewHolder =  new CheckNoteViewHolder(binding, mItemClickListenerCheckNote, mItemLongClickListener);
        }
        return viewHolder;
    }

    public void updateNote(List<Note> mItems) {
        this.mItems = mItems;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {
        return mItems.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        if(getItemViewType(position) == Constants.NOTE_DATA){
            Object obj =  mItems.get(position);
            TextNoteViewHolder textNoteViewHolder = (TextNoteViewHolder) holder;
            textNoteViewHolder.bind(obj);
            textNoteViewHolder.position = position;

        } else if(getItemViewType(position) == Constants.IMAGE_DATA){
            Object obj =  mItems.get(position);
            ImageNoteViewHolder imageNoteViewHolder = (ImageNoteViewHolder) holder;
            imageNoteViewHolder.bind(obj);
            imageNoteViewHolder.position = position;

        } else if(getItemViewType(position) == Constants.CHECK_DATA) {
            Object obj = mItems.get(position);
            CheckNoteViewHolder checkNoteViewHolder = (CheckNoteViewHolder) holder;
            checkNoteViewHolder.bind(obj);
            checkNoteViewHolder.position = position;

            checkNoteViewHolder.noteCheckCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckNote checkNote = (CheckNote) mItems.get(position);
                    if (checkNote.getCheckNote()) {
                        checkNote.setCheckNote(false);
                    } else {
                        checkNote.setCheckNote(true);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class TextNoteViewHolder extends RecyclerView.ViewHolder {
        private int position;
        final ItemNoteBinding binding;

        public  TextNoteViewHolder(final ItemNoteBinding binding,final ItemClickListenerNote mItemClickListenerNote,final ItemLongClickListener mItemLongClickListener) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     position = getAdapterPosition();
                     mItemClickListenerNote.onClickItemNote(mItems.get(position));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemLongClickListener.onLongClickItem(position);
                    return true;
                }
            });
        }
        void bind(Object obj) {
                binding.setVariable(BR.note, obj);
        }
    }

     class ImageNoteViewHolder extends RecyclerView.ViewHolder {
        private int position;
        final ItemNotePhotoBinding binding;

        public ImageNoteViewHolder(ItemNotePhotoBinding binding, final ItemLClickListenerPhotoNote mItemLClickListenerPhotoNote, final ItemLongClickListener mItemLongClickListener){
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    mItemLClickListenerPhotoNote.onClickItemPhotoNote(mItems.get(position));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemLongClickListener.onLongClickItem(position);
                    return true;
                }
            });
        }
         void bind(Object obj) {
             binding.setVariable(BR.photoNote, obj);
         }
    }

     class CheckNoteViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private CheckBox noteCheckCb;
        final ItemNoteCheckBinding binding;

        public CheckNoteViewHolder(ItemNoteCheckBinding binding, final ItemClickListenerCheckNote mItemClickListenerCheckNote, final ItemLongClickListener mItemLongClickListener){
            super(binding.getRoot());
            this.binding = binding;

            noteCheckCb = itemView.findViewById(R.id.check_list_view_item_note);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    mItemClickListenerCheckNote.onClickItemCheckNote(mItems.get(position));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemLongClickListener.onLongClickItem(position);
                    return true;
                }
            });
        }

        void bind(Object obj) {
            binding.setVariable(BR.checkNote, obj);
        }

    }

}
