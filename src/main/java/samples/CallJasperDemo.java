package samples;

import org.dwcj.App;
import org.dwcj.Environment;
import org.dwcj.bbj.BBjVar;
import org.dwcj.bridge.IDwcjBBjBridge;
import org.dwcj.component.button.Button;
import org.dwcj.component.button.event.ButtonClickEvent;
import org.dwcj.component.field.NumberField;
import org.dwcj.component.texts.Label;
import org.dwcj.component.window.Frame;
import org.dwcj.exceptions.DwcjAppInitializeException;
import org.dwcj.exceptions.DwcjRuntimeException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class CallJasperDemo extends App {

    @Override
    public void run() throws DwcjAppInitializeException {

        Frame panel = new Frame();
        panel.setStyle("padding","20px");
        panel.setStyle("display","inline-grid");
        panel.setStyle("gap","10px");

        Label headline = new Label("<html><h1>Legacy Call And Run Demo</h1>");
        panel.add(headline);

        Label explanation = new Label("<html><p>This Demo Shows how to CALL jasper.bbj</p>");
        panel.add(explanation);

        Button exec = new Button( "CALL Jasper");
        panel.add(exec);
        exec.onClick(this::onExecClick);

    }

    private void onExecClick(ButtonClickEvent buttonClickEvent) {

        IDwcjBBjBridge h = Environment.getCurrent().getDwcjHelper();
        ArrayList<BBjVar> args = new ArrayList<>();
        args.add(new BBjVar(""));

        // TODO: call should have args only optionally
        // TODO: fix working directory issue
        ArrayList<BBjVar> ret = h.call("reporting/bbjasper/chileco_report.bbj", args);


    }
}
