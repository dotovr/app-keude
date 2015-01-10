package mock;

import android.content.pm.PackageInfo;
import android.test.mock.MockPackageManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MockInstallablePackageManager extends MockPackageManager {

    private List<PackageInfo> info = new ArrayList<PackageInfo>();

    @Override
    public List<PackageInfo> getInstalledPackages(int flags) {
        return info;
    }

    public void install(String id, int version, String versionName) {
        PackageInfo existing = getPackageInfo(id);
        if (existing != null) {
            existing.versionCode = version;
            existing.versionName = versionName;
        } else {
            PackageInfo p = new PackageInfo();
            p.packageName = id;
            p.versionCode = version;
            p.versionName = versionName;
            info.add(p);
        }
    }

    public PackageInfo getPackageInfo(String id) {
        for (PackageInfo i : info) {
            if (i.packageName.equals(id)) {
                return i;
            }
        }
        return null;
    }

    public void remove(String id) {
        for (Iterator<PackageInfo> it = info.iterator(); it.hasNext();) {
            PackageInfo info = it.next();
            if (info.packageName.equals(id)) {
                it.remove();
                return;
            }
        }
    }

}
