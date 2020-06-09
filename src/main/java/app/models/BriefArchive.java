package app.models;

import com.google.gson.annotations.Expose;
import java.util.List;
import java.util.stream.Collectors;

public class BriefArchive {

    @Expose
    private BriefDocument document;

    @Expose
    private Department department;

    @Expose
    private Performer performer;

    private BriefArchive() {

    }

    public BriefDocument getDocument() {
        return this.document;
    }

    public void setDocument(final BriefDocument document) {
        this.document = document;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public Performer getPerformer() {
        return this.performer;
    }

    public void setPerformer(final Performer performer) {
        this.performer = performer;
    }

    public static BriefArchive toBriefArchive(BriefDocument doc) {
        BriefArchive arch = new BriefArchive();
        arch.setDocument(doc);
        Performer oldP = doc.getPerformer();
        Performer performer = new Performer();
        performer.setId(oldP.getId());
        performer.setName(oldP.getName());
        arch.setPerformer(performer);
        Department department = new Department();
        Department oldD = oldP.getDepartment();
        department.setId(oldD.getId());
        department.setName(oldD.getName());
        arch.setDepartment(department);
        return arch;
    }

    public static List<BriefArchive> toBriefArchive(List<BriefDocument> docs) {
        return docs.parallelStream().map(doc -> {
            return toBriefArchive(doc);
        }).collect(Collectors.toList());
    }
}
