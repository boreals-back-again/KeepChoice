package net.enderbyteprograms.KeepChoice.Language;

import java.util.HashMap;
import java.util.Map;

public enum Languages {
    English ("en"),
    French ("fr"),
    Spanish ("es"),
    Russian ("ru"),
    Italian ("it"),
    Polish ("pl"),
    Custom ("xx");

    private static Map<String,Languages> revlookup = new HashMap<>();

    static {
        for (Languages l : Languages.values()) {
            revlookup.put(l.lncode,l);
        }
    }

    private String lncode;
    Languages(String ln) {
        this.lncode = ln;
    }
    public String value() {
        return this.lncode;
    }

    public static Languages GetFromString(String val) {
        if (revlookup.containsKey(val)) {
            return revlookup.get(val);
        } else {
            return Languages.Custom;
        }
    }
}
