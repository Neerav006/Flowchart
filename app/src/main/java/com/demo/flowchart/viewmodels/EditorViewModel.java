package com.demo.flowchart.viewmodels;

import static com.demo.flowchart.drawing.WorkspaceView.BOLD_LINE_WIDTH;
import static com.demo.flowchart.drawing.WorkspaceView.WORKSPACE_HEIGHT;
import static com.demo.flowchart.drawing.WorkspaceView.WORKSPACE_WIDTH;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.demo.flowchart.App;
import com.demo.flowchart.auth.FirebaseRepository;
import com.demo.flowchart.database.FlowchartDao;
import com.demo.flowchart.database.FlowchartEntity;
import com.demo.flowchart.drawing.model.DrawingBlock;
import com.demo.flowchart.util.Converters;
import com.demo.flowchart.util.JsonService;
import com.demo.flowchart.model.Workspace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorViewModel extends ViewModel {

    private final FlowchartDao flowchartDao;
    private final JsonService jsonService;
    private FlowchartEntity flowchartEntity;
    private final FirebaseRepository authRepo;

    public EditorViewModel() {
        authRepo = App.getInstance().getFirebase();
        flowchartDao = App.getInstance().getDatabase().flowchartDao();
        jsonService = new JsonService();
    }

    public void saveWorkspace(Workspace workspace) {
        String json = jsonService.flowchartToJson(workspace);
        flowchartEntity.setJson(json);
        flowchartDao.update(flowchartEntity);
        authRepo.uploadFlowchartToFirebase(flowchartEntity);
    }

    public Workspace loadWorkspace(long id) {
        flowchartEntity = flowchartDao.get(id);
        return jsonService.jsonToFlowchart(flowchartEntity.getJson());
    }

    public boolean viewToPdf(Workspace workspace) {
        Map<Long, DrawingBlock> drawingBlocks = Converters.blocksToDrawingBlocks(workspace.getBlocks());

        Paint blockPaint = new Paint();
        blockPaint.setColor(Color.BLACK);
        blockPaint.setStyle(Paint.Style.STROKE);
        blockPaint.setStrokeWidth(BOLD_LINE_WIDTH / workspace.getScale());

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "report.pdf");
        PdfDocument document = new PdfDocument();

        try (FileOutputStream out = new FileOutputStream(file)) {
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(WORKSPACE_WIDTH, WORKSPACE_HEIGHT, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas c = page.getCanvas();

            for (DrawingBlock drawingBlock : drawingBlocks.values()) {
                drawingBlock.draw(c, blockPaint);
            }

            c.save();
            document.finishPage(page);
            document.writeTo(out);
            out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return false;
    }
}
