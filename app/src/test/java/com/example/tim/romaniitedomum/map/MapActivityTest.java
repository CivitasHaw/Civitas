package com.example.tim.romaniitedomum.map;

import com.example.tim.romaniitedomum.Util.BcAc;
import com.example.tim.romaniitedomum.Util.Util;
import com.example.tim.romaniitedomum.artefact.Artefact;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MapActivityTest {

    MapActivity SUT = new MapActivity();
    List<Artefact> list = new ArrayList<>();

    @Before
    public void prepareList(){

        Artefact artefact = new Artefact();
        artefact.setArtefactName("Civitas");
        artefact.setCategoryName(Util.CATEGORY_SPIELSTAETTE);
        artefact.setAnnoDomini(BcAc.AFTER_CHRIST.toString());
        artefact.setArtefactImageUrl("");
        artefact.setArtefactAudioUrl("");
        artefact.setArtefactDescription("greatest app ever");
        artefact.setAuthorName("Marcus Tullio Cicero");
        artefact.setArtefactAge("63");
        artefact.setLatitude(53.50000);
        artefact.setLongitude(10.000);
        list.add(artefact);

        artefact = new Artefact();
        artefact.setArtefactName("Civitas2");
        artefact.setCategoryName(Util.CATEGORY_BASILIKA);
        artefact.setAnnoDomini(BcAc.AFTER_CHRIST.toString());
        artefact.setArtefactImageUrl("");
        artefact.setArtefactAudioUrl("");
        artefact.setArtefactDescription("greatest app ever");
        artefact.setAuthorName("Cesar");
        artefact.setArtefactAge("100");
        artefact.setLatitude(53.50000);
        artefact.setLongitude(10.000);
        list.add(artefact);

        artefact = new Artefact();
        artefact.setArtefactName("Civitas3");
        artefact.setCategoryName(Util.CATEGORY_SPIELSTAETTE);
        artefact.setAnnoDomini(BcAc.BEFORE_CHRIST.toString());
        artefact.setArtefactImageUrl("");
        artefact.setArtefactAudioUrl("");
        artefact.setArtefactDescription("greatest app ever");
        artefact.setAuthorName("Pompeius");
        artefact.setArtefactAge("100");
        artefact.setLatitude(53.50000);
        artefact.setLongitude(10.000);
        list.add(artefact);

        artefact = new Artefact();
        artefact.setArtefactName("Civitas4");
        artefact.setCategoryName(Util.CATEGORY_BASILIKA);
        artefact.setAnnoDomini(BcAc.BEFORE_CHRIST.toString());
        artefact.setArtefactImageUrl("");
        artefact.setArtefactAudioUrl("");
        artefact.setArtefactDescription("greatest app ever");
        artefact.setAuthorName("Pontius");
        artefact.setArtefactAge("50");
        artefact.setLatitude(53.50000);
        artefact.setLongitude(10.000);
        list.add(artefact);

    }

    @After
    public void clearList(){
        list.clear();
    }

    @Test
    // @DisplayName("Hier kann man eine Testmethode sauber beschreiben.")
    public void filteredListIsNotEmpty(){
        List<Artefact> result = SUT.filterArtefactList(list, BcAc.AFTER_CHRIST, 65, false);

        assertTrue(!result.isEmpty());
    }

    @Test
    public void filterAge_BcToBc_ResultSize1(){
        List<Artefact> result = SUT.filterArtefactList(list, BcAc.BEFORE_CHRIST, 65, false);

        assertTrue(result.size() == 1);
    }

    @Test
    public void filterAge_BcToAc_ResultSize2(){
        List<Artefact> result = SUT.filterArtefactList(list, BcAc.BEFORE_CHRIST, 65, false);
        List<Artefact> result2 = SUT.filterArtefactList(list, BcAc.AFTER_CHRIST, 65, false);
        result.addAll(result2);

        assertTrue(result.size() == 2);
    }

    @Test
    public void filterAge_AcToAc_ResultSize1(){
        List<Artefact> result = SUT.filterArtefactList(list, BcAc.AFTER_CHRIST, 65, false);

        assertTrue(result.size() == 1);
    }

    @Test
    public void filterAge_AcToAc_EdgeCaseResultIsEmpty(){
        List<Artefact> result = SUT.filterArtefactList(list, BcAc.AFTER_CHRIST, 63, false);

        assertTrue(result.isEmpty());
    }

    @Test
    public void filterAge_BcToBc_EdgeCaseResultIsEmpty(){
        List<Artefact> result = SUT.filterArtefactList(list, BcAc.BEFORE_CHRIST, 50, false);

        assertTrue(result.isEmpty());
    }

    @Test
    public void filterAge_BcToAc_EdgeCaseResultIsEmpty(){
        List<Artefact> result = SUT.filterArtefactList(list, BcAc.BEFORE_CHRIST, 50, false);
        List<Artefact> result2 = SUT.filterArtefactList(list, BcAc.AFTER_CHRIST, 63, false);
        result.addAll(result2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void filterCategory_SpielStaette_ResultSize2(){
        List<Artefact> result = SUT.filterCategory(list, Util.CATEGORY_SPIELSTAETTE);

        assertTrue(result.size() == 2);
    }

    @Test
    public void filterCategory_WrongInput_ResultIsEmpty(){
        List<Artefact> result = SUT.filterCategory(list, "foobar");

        assertTrue(result.isEmpty());
    }

}