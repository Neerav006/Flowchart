package com.demo.flowchart.editor.view;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.demo.flowchart.App;
import com.demo.flowchart.database.FlowchartDao;
import com.demo.flowchart.database.FlowchartEntity;
import com.demo.flowchart.editor.model.DecisionDrawingBlock;
import com.demo.flowchart.editor.model.DrawingBlock;
import com.demo.flowchart.editor.model.IODrawingBlock;
import com.demo.flowchart.editor.model.PredefinedProcessDrawingBlock;
import com.demo.flowchart.editor.model.ProcessDrawingBlock;
import com.demo.flowchart.editor.model.TerminalDrawingBlock;
import com.demo.flowchart.json.Block;
import com.demo.flowchart.json.BlockType;
import com.demo.flowchart.json.Flowline;
import com.demo.flowchart.json.JsonService;

import java.util.ArrayList;
import java.util.List;

public class EditorViewModel extends ViewModel {

    private final FlowchartDao flowchartDao;
    private final JsonService jsonService;
    private FlowchartEntity flowchartEntity;
    private Block[] blocks;

    public EditorViewModel() {
        flowchartDao = App.getInstance().getDatabase().flowchartDao();
        jsonService = new JsonService();
    }

    public List<DrawingBlock> getDrawingBlocks() {
        List<DrawingBlock> drawingBlocks = new ArrayList<>();
        for (Block block: blocks) {
            drawingBlocks.add(blockToDrawing(block));
        }
        return drawingBlocks;
    }

    public void setDrawingBlocks(List<DrawingBlock> drawingBlocks) {
        int size = drawingBlocks.size();
        blocks = new Block[size];
        for (int i = 0; i < size; i++) {
            blocks[i] = drawingToBlock(drawingBlocks.get(i));
        }
    }

    public void saveFlowchart() {
        String json = jsonService.flowchartToJson(blocks);
        flowchartEntity.setJson(json);
        Log.d("SUKA", json);
        flowchartDao.update(flowchartEntity);
    }

    public void loadFlowchart(long id) {
        flowchartEntity = flowchartDao.get(id);
        blocks = jsonService.jsonToFlowchart(flowchartEntity.getJson());
    }

    private DrawingBlock blockToDrawing(Block block) {
        DrawingBlock drawingBlock = null;

        long id = block.getId();
        BlockType type = block.getType();
        int startX = block.getStartX();
        int startY = block.getStartY();
        int width = block.getWidth();
        int height = block.getHeight();
        String text = block.getText();
        List<Flowline> flowlines = block.getFlowlines();

        switch (type) {
            case TERMINAL: {
                drawingBlock = new TerminalDrawingBlock(
                        startX, startY, width, height
                );
                break;
            }
            case PROCESS: {
                drawingBlock = new ProcessDrawingBlock(
                        startX, startY, width, height
                );
                break;
            }
            case PREDEFINED_PROCESS: {
                drawingBlock = new PredefinedProcessDrawingBlock(
                        startX, startY, width, height
                );
                break;
            }
            case DECISION: {
                drawingBlock = new DecisionDrawingBlock(
                        startX, startY, width, height
                );
                break;
            }
            case IO: {
                drawingBlock = new IODrawingBlock(
                        startX, startY, width, height
                );
                break;
            }
        }

        return drawingBlock;
    }

    private Block drawingToBlock(DrawingBlock drawingBlock) {
        BlockType type = null;
        if (drawingBlock instanceof TerminalDrawingBlock) {
            type = BlockType.TERMINAL;
        } else if (drawingBlock instanceof PredefinedProcessDrawingBlock) {
            type = BlockType.PREDEFINED_PROCESS;
        } else if (drawingBlock instanceof ProcessDrawingBlock) {
            type = BlockType.PROCESS;
        } else if (drawingBlock instanceof DecisionDrawingBlock) {
            type = BlockType.DECISION;
        } else if (drawingBlock instanceof IODrawingBlock) {
            type = BlockType.IO;
        }

        return new Block(
                drawingBlock.getId(),
                type,
                drawingBlock.getStartX(),
                drawingBlock.getStartY(),
                drawingBlock.getWidth(),
                drawingBlock.getHeight(),
                drawingBlock.getText(),
                null // TODO dodelat'
        );
    }
}
