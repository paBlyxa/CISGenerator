package com.we.cisgenerator.model.winccoa;

import com.we.cisgenerator.model.PA;
import com.we.cisgenerator.model.winccoa.ascii.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SDS_PA extends PA implements Dp {

    private final static String TYPE_NAME = "SDS_PA";

    public String exportName = null;

    public SDS_PA() {

    }

    public SDS_PA(String ident) {
        setIdent(ident);
    }

    public SDS_PA(String ident, String type, String name, String formula, String format, String... tags) {
        setIdent(ident);
        setType(type);
        setName(name);
        setFormula(formula);
        setFormat(format);
        for (String tag : tags) {
            addTag(tag);
        }
    }

    @Override
    public String getExportField(AsciiExportField field) {
        StringBuilder str = new StringBuilder();
        if (exportName == null) {
            exportName = "ASC (1)/0\t" + getIdent() + ".val\t" + TYPE_NAME + "\t";
        }
        switch (field) {
            case AlertValue:
                return exportName + "\t13\t\t\t\t\t\"\"\t\"\"\tlt:1 LANG:10027 \"\"\t\\0\t\t\t1\t1\t\t\t\t0\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0\r\n"
                        + exportName + "1\t4\t-1.7976931348623e+308\t-99000\t0\t1\t\t\t\t\talert.\tlt:1 LANG:10027 \"Нижний аварийный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t-1.7976931348623e+308\t-99000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n"
                        + exportName + "2\t4\t-99000\t-98000\t0\t1\t\t\t\t\twarning.\tlt:1 LANG:10027 \"Нижний предупредительный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t-99000\t-98000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n"
                        + exportName + "3\t4\t-98000\t98000\t0\t1\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t-98000\t98000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n"
                        + exportName + "4\t4\t98000\t99000\t0\t1\t\t\t\t\twarning.\tlt:1 LANG:10027 \"Верхний предупредительный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t98000\t99000\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n"
                        + exportName + "5\t4\t99000\t1.7976931348623e+308\t0\t0\t\t\t\t\talert.\tlt:1 LANG:10027 \"Верхний аварийный предел\"\t\t\t\t0\t01.01.1970 00:00:00.000\t\t99000\t1.7976931348623e+308\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tlt:1 LANG:10027 \"\"\t\t0x0\t\t\t\t\r\n";
            case Aliases:
                return getIdent() + ".\t\"\"\tlt:1 LANG:10027 \"" + getName() + "@%s@\"\r\n"
                        + getIdent() + ".val\t\"\"\tlt:1 LANG:10027 \"" + getName() + "@%" + getFormat() + "@" + (getUnits()) + "\"\r\n";
            case Datapoint:
                return getIdent() + "\t" + TYPE_NAME + "\t" + /*dp.getId() +*/ "\r\n";
            case DbArchiveInfo:
                ExportField<DBArchiveInfoE> archiveInfo = new ExportField<>(DBArchiveInfoE.values().length);
                archiveInfo.setElement(DBArchiveInfoE.ElementName, getIdent() + ".val")
                        .setElement(DBArchiveInfoE.TypeName, TYPE_NAME)
                        .setElement(DBArchiveInfoE._archive_type, "45")
                        .setElement(DBArchiveInfoE._archive_archive, "1");
                str.append(archiveInfo.toExportString());
                archiveInfo.setElement(DBArchiveInfoE.DetailNr, "1")
                        .setElement(DBArchiveInfoE._archive_type, "3")
                        .setElement(DBArchiveInfoE._archive_archive, "")
                        .setElement(DBArchiveInfoE._archive_class, "_ValueArchive_4")
                        .setElement(DBArchiveInfoE._archive_interv, "0")
                        .setElement(DBArchiveInfoE._archive_interv_type, "0")
                        .setElement(DBArchiveInfoE._archive_std_type, "6")
                        .setElement(DBArchiveInfoE._archive_std_tol, "0")
                        .setElement(DBArchiveInfoE._archive_std_time, "01.01.1970 00:00:01.000")
                        .setElement(DBArchiveInfoE._archive_round_val, "0")
                        .setElement(DBArchiveInfoE._archive_round_inv, "0");
                str.append(archiveInfo.toExportString());
                return str.toString();
            //return exportName + "\t45\t0\t\t\t\t\t\t\t\r\n"
            //		+ exportName + "1\t15\t\t_ValueArchive_4\t0\t0\t0\t0\t01.01.1970 00:00:00.000\t0\t0\r\n";
            case DistributionInfo:
                return "";
            case DpDefaultValue:
                ExportField<DpDefaultValueE> defValue = new ExportField<>(DpDefaultValueE.values().length);
                defValue.setElement(DpDefaultValueE.TypeName, TYPE_NAME)
                        .setElement(DpDefaultValueE.ElementName, getIdent() + ".val")
                        .setElement(DpDefaultValueE._default_type, "3")
                        .setElement(DpDefaultValueE._default_value, getDefaultValue().toString())
                        .setElement(DpDefaultValueE._default_set_ibit, "0")
                        .setElement(DpDefaultValueE._default_set_pvrange, "0");
                return defValue.toExportString();
            case PvssRangeCheck:
                ExportField<RangeCheckE> rangeCheck = new ExportField<>(RangeCheckE.values().length);
                rangeCheck.setElement(RangeCheckE.TypeName, TYPE_NAME)
                        .setElement(RangeCheckE.ElementName, getIdent() + ".val")
                        .setElement(RangeCheckE._pv_range_type, "8")
                        .setElement(RangeCheckE._pv_range_ignor_inv, "0")
                        .setElement(RangeCheckE._pv_range_neg, "1")
                        .setElement(RangeCheckE._pv_range_set, "-9999");
                return rangeCheck.toExportString();
            case PeriphAddrMain:
            case DpConvRawToIngMain:
                return "";
            case DpFunction:
                return exportName + "60\t" + getExportTags() + "\t\"" + getFormula() + "\"\t\t1\r\n";
            //return exportName + "60\t" + "_Event.Heartbeat:_original.._value" + "\t\"" + getFormula() + "\"\t\t1\r\n";
            case DpType:
                return "SDS_PA.SDS_PA\t1#1\r\n"
                        + "\tval\t22#4\r\n"
                        + "\tinfo\t1#5\r\n"
                        + "\t\ttype\t25#6\r\n"
                        + "\t\tsubtype\t25#7\r\n"
                        + "\t\tsystem\t25#8\r\n"
                        + "\t\tremarks\t25#9\r\n"
                        + "\tlogcont\t25#16\r\n"
                        + "\talarm\t1#17\r\n"
                        + "\t\thAlarm\t1#18\r\n"
                        + "\t\t\tx_hAlarm\t14#30\r\n"
                        + "\t\t\t\tmode1\t22#31\r\n"
                        + "\t\t\t\tmode2\t22#32\r\n"
                        + "\t\t\t\tmode3\t22#33\r\n"
                        + "\t\t\t\tmode4\t22#34\r\n"
                        + "\t\t\t\tmode5\t22#35\r\n"
                        + "\t\t\t\tmode6\t22#36\r\n"
                        + "\t\t\tz_hAlarm\t17#37\r\n"
                        + "\t\t\t\tmode1\t25#38\r\n"
                        + "\t\t\t\tmode2\t25#39\r\n"
                        + "\t\t\t\tmode3\t25#40\r\n"
                        + "\t\t\t\tmode4\t25#41\r\n"
                        + "\t\t\t\tmode5\t25#42\r\n"
                        + "\t\t\t\tmode6\t25#43\r\n"
                        + "\t\thWarn\t1#21\r\n"
                        + "\t\t\tx_hWarn\t14#44\r\n"
                        + "\t\t\t\tmode1\t22#45\r\n"
                        + "\t\t\t\tmode2\t22#46\r\n"
                        + "\t\t\t\tmode3\t22#47\r\n"
                        + "\t\t\t\tmode4\t22#48\r\n"
                        + "\t\t\t\tmode5\t22#49\r\n"
                        + "\t\t\t\tmode6\t22#50\r\n"
                        + "\t\t\tz_hWarn\t17#51\r\n"
                        + "\t\t\t\tmode1\t25#52\r\n"
                        + "\t\t\t\tmode2\t25#53\r\n"
                        + "\t\t\t\tmode3\t25#54\r\n"
                        + "\t\t\t\tmode4\t25#55\r\n"
                        + "\t\t\t\tmode5\t25#56\r\n"
                        + "\t\t\t\tmode6\t25#57\r\n"
                        + "\t\tlWarn\t1#24\r\n"
                        + "\t\t\tx_lWarn\t14#58\r\n"
                        + "\t\t\t\tmode1\t22#59\r\n"
                        + "\t\t\t\tmode2\t22#60\r\n"
                        + "\t\t\t\tmode3\t22#61\r\n"
                        + "\t\t\t\tmode4\t22#62\r\n"
                        + "\t\t\t\tmode5\t22#63\r\n"
                        + "\t\t\t\tmode6\t22#64\r\n"
                        + "\t\t\tz_lWarn\t17#65\r\n"
                        + "\t\t\t\tmode1\t25#66\r\n"
                        + "\t\t\t\tmode2\t25#67\r\n"
                        + "\t\t\t\tmode3\t25#68\r\n"
                        + "\t\t\t\tmode4\t25#69\r\n"
                        + "\t\t\t\tmode5\t25#70\r\n"
                        + "\t\t\t\tmode6\t25#71\r\n"
                        + "\t\tlAlarm\t1#27\r\n"
                        + "\t\t\tx_lAlarm\t14#72\r\n"
                        + "\t\t\t\tmode1\t22#73\r\n"
                        + "\t\t\t\tmode2\t22#74\r\n"
                        + "\t\t\t\tmode3\t22#75\r\n"
                        + "\t\t\t\tmode4\t22#76\r\n"
                        + "\t\t\t\tmode5\t22#77\r\n"
                        + "\t\t\t\tmode6\t22#78\r\n"
                        + "\t\t\tz_lAlarm\t17#79\r\n"
                        + "\t\t\t\tmode1\t25#80\r\n"
                        + "\t\t\t\tmode2\t25#81\r\n"
                        + "\t\t\t\tmode3\t25#82\r\n"
                        + "\t\t\t\tmode4\t25#83\r\n"
                        + "\t\t\t\tmode5\t25#84\r\n"
                        + "\t\t\t\tmode6\t25#85\r\n";
            case DpValue:
                String dateTimeStr = "\t0x8300000000000001\t"
                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS")) + "\r\n";
                return "ASC (1)/0\t" + getIdent() + ".info.type\t" + TYPE_NAME + "\t" + getType() + dateTimeStr
                        + getAlarms(TYPE_NAME);
        }
        return null;
    }

    @Override
    public String getDpName() {
        return getIdent();
    }

    @Override
    public String getSDSType() {
        return TYPE_NAME;
    }

    @Override
    public boolean isCorrect() {
        return (getIdent() != null) && (!getFormula().isEmpty());
    }


}
