package com.avricot.cboost.service.project;

import com.avricot.cboost.domain.project.FieldType;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class ProjectServiceTest {

    private ProjectService service = new ProjectService();
    @Test
    public void testGuessHeadFieldType() throws Exception {
        Assert.assertEquals(FieldType.NAME,service.guessHeadFieldType("raison sociale"));
        Assert.assertEquals(FieldType.ADDRESS,service.guessHeadFieldType("adresse"));
        Assert.assertEquals(FieldType.ID,service.guessHeadFieldType("siret/siren"));
        Assert.assertEquals(FieldType.ZIP_CODE,service.guessHeadFieldType("code postal"));
        Assert.assertNull(service.guessHeadFieldType("code naf"));
        Assert.assertEquals(FieldType.COUNTRY,service.guessHeadFieldType("pays"));
        Assert.assertNull(service.guessHeadFieldType("je sais pas trop quoi"));
    }

    @Test
    public void testGuessFieldType() throws Exception {
        Assert.assertEquals(FieldType.ADDRESS,service.guessFieldType("12 rue de blabla"));
        Assert.assertEquals(FieldType.ID,service.guessFieldType("351257217"));
        Assert.assertEquals(FieldType.ID,service.guessFieldType("35125721700015"));
        Assert.assertEquals(FieldType.ID,service.guessFieldType("FR38351257217"));
        Assert.assertEquals(FieldType.ZIP_CODE,service.guessFieldType("75010"));
        Assert.assertEquals(null,service.guessFieldType("je sais pas trop quoi"));
    }

    @Test
    public void testMatchLineContent() throws Exception {
        final List<String[]> lines = Lists.newArrayList(new String[]{"35125721700015", "12 rue de machin", "France", "75010"},
                                                        new String[]{"351257217", "av de truc", "France", "BZ7510"});
        Map<Integer, Map<FieldType, Integer>> match = service.matchLineContent(lines);
        Assert.assertEquals(Integer.valueOf(2), match.get(0).get(FieldType.ID));
        Assert.assertEquals(1, match.get(0).size());

        Assert.assertEquals(Integer.valueOf(2), match.get(1).get(FieldType.ADDRESS));
        Assert.assertEquals(1, match.get(1).size());

        Assert.assertEquals(Integer.valueOf(1), match.get(3).get(FieldType.ZIP_CODE));
        Assert.assertEquals(1, match.get(3).size());
        Assert.assertNull(match.get(2));
    }

}
