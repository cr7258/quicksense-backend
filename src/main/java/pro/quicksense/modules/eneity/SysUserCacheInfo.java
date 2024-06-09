package pro.quicksense.modules.eneity;

import lombok.Data;

import java.util.List;

@Data
public class SysUserCacheInfo {
    private String sysUserCode;

    private String sysUserName;

    private String sysOrgCode;

    private List<String> sysMultiOrgCode;

    private boolean oneDepart;
}
