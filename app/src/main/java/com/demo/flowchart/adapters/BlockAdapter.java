package com.demo.flowchart.adapters;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.flowchart.R;
import com.demo.flowchart.drawing.BlockView;
import com.demo.flowchart.drawing.model.DecisionDrawingBlock;
import com.demo.flowchart.drawing.model.DrawingBlock;
import com.demo.flowchart.drawing.model.IODrawingBlock;
import com.demo.flowchart.drawing.model.PredefinedProcessDrawingBlock;
import com.demo.flowchart.drawing.model.ProcessDrawingBlock;
import com.demo.flowchart.drawing.model.TerminalDrawingBlock;

import java.util.ArrayList;
import java.util.List;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockViewHolder> {

    List<DrawingBlock> drawingBlocks;

    public BlockAdapter() {
        this.drawingBlocks = getBlockSamples();
    }

    @NonNull
    @Override
    public BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View blockView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_block, parent, false);
        return new BlockViewHolder(blockView);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockViewHolder holder, int position) {
        holder.bind(drawingBlocks.get(position));
    }

    @Override
    public int getItemCount() {
        return drawingBlocks.size();
    }

    protected static class BlockViewHolder extends RecyclerView.ViewHolder {

        BlockView blockView;

        public BlockViewHolder(@NonNull View itemView) {
            super(itemView);
            this.blockView = (BlockView) itemView;
        }

        protected void bind(DrawingBlock drawingBlock) {
            blockView.setBlock(drawingBlock);
            blockView.setTag(drawingBlock.getClass().getSimpleName());

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

    private List<DrawingBlock> getBlockSamples() {
        List<DrawingBlock> drawingBlocks = new ArrayList<>();
        drawingBlocks.add(new TerminalDrawingBlock());
        drawingBlocks.add(new ProcessDrawingBlock());
        drawingBlocks.add(new PredefinedProcessDrawingBlock());
        drawingBlocks.add(new DecisionDrawingBlock());
        drawingBlocks.add(new IODrawingBlock());
        return drawingBlocks;
    }
}
