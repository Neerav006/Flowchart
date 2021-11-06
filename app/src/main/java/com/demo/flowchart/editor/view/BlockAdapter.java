package com.demo.flowchart.editor.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.flowchart.R;
import com.demo.flowchart.editor.model.Block;
import com.demo.flowchart.editor.model.DecisionBlock;
import com.demo.flowchart.editor.model.IOBlock;
import com.demo.flowchart.editor.model.PredefinedProcessBlock;
import com.demo.flowchart.editor.model.ProcessBlock;
import com.demo.flowchart.editor.model.TerminalBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockViewHolder> {

    List<Block> blocks;

    public BlockAdapter() {
        this.blocks = getBlockSamples();
    }

    @NonNull
    @Override
    public BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View blockView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_item, parent, false);
        return new BlockViewHolder(blockView);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockViewHolder holder, int position) {
        holder.bind(blocks.get(position));
    }

    @Override
    public int getItemCount() {
        return blocks.size();
    }

    protected static class BlockViewHolder extends RecyclerView.ViewHolder {

        BlockView blockView;

        public BlockViewHolder(@NonNull View itemView) {
            super(itemView);
            this.blockView = (BlockView) itemView;
        }

        protected void bind(Block block) {
            blockView.setBlock(block);
            blockView.setTag(block.getClass().getSimpleName());

            blockView.setOnLongClickListener(view -> {

                ClipData.Item item = new ClipData.Item((String.valueOf(view.getTag())));
                ClipData data = new ClipData(
                        String.valueOf(view.getTag()),
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                return view.startDrag(
                        data, shadow, null, 0
                );
            });

        }
    }

    private List<Block> getBlockSamples() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(new TerminalBlock());
        blocks.add(new ProcessBlock());
        blocks.add(new PredefinedProcessBlock());
        blocks.add(new DecisionBlock());
        blocks.add(new IOBlock());
        return blocks;
    }
}
