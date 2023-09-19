package org.dwcj.extendeddemos.call;

import org.dwcj.App;
import org.dwcj.Environment;
import org.dwcj.bbj.BBjVar;
import org.dwcj.bridge.IDwcjBBjBridge;
import org.dwcj.component.button.Button;

import org.dwcj.component.button.event.ButtonClickEvent;
import org.dwcj.component.field.NumberField;
import org.dwcj.component.field.TextField;
import org.dwcj.component.texts.Label;
import org.dwcj.exceptions.DwcjAppInitializeException;
import org.dwcj.component.window.Frame;
import org.dwcj.exceptions.DwcjRuntimeException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class CallDemo extends App {

    private NumberField ipa;
    private NumberField ipb;
    private String programsource;
    private Label result;

    @Override
    public void run() throws DwcjAppInitializeException {

        Frame panel = new Frame();
        panel.setStyle("padding","20px");
        panel.setStyle("display","inline-grid");
        panel.setStyle("gap","10px");

        Label headline = new Label("<html><h1>Legacy Call And Run Demo</h1>");
        panel.add(headline);

        Label explanation = new Label("<html><p>This Demo Shows how to invoke the following legacy CALL public program.</p>");
        panel.add(explanation);

        programsource = "0010 ENTER A,B%,C%,D$\n"+
                "0020 LET C% = INT(A * B%)\n"+
                "0030 LET D$=\"The Result is \"+STR(C%)+\".\"\n"+
                "0035 A=MSGBOX(D$)\n"+
                "0040 EXIT\n";

        //this could later be changed to a MultiLineEdit so the user of this demo can edit the call
        Label sourcecode = new Label(programsource);
        sourcecode.setStyle("font-family","monospace");
        panel.add(sourcecode);

        ipa = new NumberField("4.56");
        ipa.setStep(0.01);
        ipa.setLabel("Value for A (Numeric)");
        ipa.setStyle("padding-top","40px");
        panel.add(ipa);

        ipb = new NumberField();
        ipb.setLabel("Value for B% (Integer)");
        ipb.setStyle("padding-top","20px");
        panel.add(ipb);

        Button exec = new Button("Make CALL");
        panel.add(exec);
        exec.onClick(this::onExecClick);
        
        result = new Label("");
        result.setStyle("font-family","monospace");
        panel.add(result);

    }

    private void onExecClick(ButtonClickEvent buttonClickEvent) {

        File f = null;
        try {
            f = File.createTempFile("pgm_",".bbj");
            f.deleteOnExit();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
            writer.write(this.programsource);
            writer.close();
        } catch (IOException e) {
            msgbox(e.getMessage(),0,"Could not create program file");
            throw new DwcjRuntimeException(e);
        }


        ArrayList<BBjVar> args = new ArrayList<>();
        args.add(new BBjVar(new BigDecimal(ipa.getText())));
        args.add(new BBjVar(Integer.valueOf(ipb.getText())));
        args.add(new BBjVar(0));
        args.add(new BBjVar(""));

        IDwcjBBjBridge h = Environment.getCurrent().getDwcjHelper();
        ArrayList<BBjVar> ret = h.call(f.getAbsolutePath(), args);

        String r = "Result: \n";
        r = r+"A="+ret.get(0).getNumVal()+"\n";
        r = r+"B%="+ret.get(1).getIntVal()+"\n";
        r = r+"C%="+ret.get(2).getIntVal()+"\n";
        r = r+"D$="+ret.get(3).getStrVal()+"\n";
        result.setText(r);


    }
}
