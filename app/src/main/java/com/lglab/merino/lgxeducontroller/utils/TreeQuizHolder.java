package com.lglab.merino.lgxeducontroller.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lglab.merino.lgxeducontroller.R;
import com.lglab.merino.lgxeducontroller.activities.CreateQuestionActivity;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Albert Merino on 02/10/18.
 */
public class TreeQuizHolder extends TreeNode.BaseNodeViewHolder<TreeQuizHolder.IconTreeItem> {
    private ImageView arrowView;

    public TreeQuizHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, final IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.treeview_node_game, null, false);
        TextView tvValue = view.findViewById(R.id.node_value);
        tvValue.setText(value.text);


        final ImageView iconView = view.findViewById(R.id.imageIcon);
        iconView.setImageDrawable(context.getResources().getDrawable(value.icon));

        arrowView = view.findViewById(R.id.arrow_icon);
        ImageView addPOIButton = view.findViewById(R.id.btn_addPOI);
        ImageView editButton = view.findViewById(R.id.btn_edit);
        ImageView deleteButton = view.findViewById(R.id.btn_delete);

        switch (value.type) {
            case SUBJECT:
                addPOIButton.setVisibility(View.VISIBLE);
                addPOIButton.setOnClickListener(view1 -> {
                    showToast("Add Game");
                });
                break;
            case GAME:
                addPOIButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);

                addPOIButton.setOnClickListener(view12 -> {
                    showToast("Add Question");
                    Intent intent = new Intent(context, CreateQuestionActivity.class);
//                    context.startActivity(intent);
                });
                deleteButton.setOnClickListener(view12 -> {
                    showToast("Delete Game");
                });
                break;
            case QUESTION:
                arrowView.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);

                if (node.getId() % 2 == 0) {
                    view.setBackgroundColor(context.getResources().getColor(R.color.white));
                }

                editButton.setOnClickListener(view12 -> {
                    showToast("Edit question");
                });
                deleteButton.setOnClickListener(view12 -> {
                    showToast("Delete Question");
                });
                break;
        }
        return view;
    }

    private void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setImageDrawable(context.getResources().getDrawable(active ? R.drawable.ic_keyboard_arrow_down_black_36dp : R.drawable.ic_keyboard_arrow_right_black_36dp));
    }

    public enum TreeQuizType {SUBJECT, GAME, QUESTION, NONE}

    public static class IconTreeItem {
        public String text;
        public int icon;
        public TreeQuizType type;
        public long id;

        public IconTreeItem(int icon, String text, long id, TreeQuizType type) {
            this.icon = icon;
            this.text = text;
            this.type = type;
            this.id = id;
        }
    }


}
