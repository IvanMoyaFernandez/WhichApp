package com.which.whichapp.web.rest;

import com.which.whichapp.WhichApp;

import com.which.whichapp.domain.Smartphone;
import com.which.whichapp.repository.SmartphoneRepository;
import com.which.whichapp.service.SmartphoneService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.which.whichapp.domain.enumeration.EnumMarca;
import com.which.whichapp.domain.enumeration.EnumResolucionPantalla;
import com.which.whichapp.domain.enumeration.EnumOS;
/**
 * Test class for the SmartphoneResource REST controller.
 *
 * @see SmartphoneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WhichApp.class)
public class SmartphoneResourceIntTest {

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final EnumMarca DEFAULT_MARCA = EnumMarca.Samsung;
    private static final EnumMarca UPDATED_MARCA = EnumMarca.Apple;

    private static final Double DEFAULT_CAMARA = 1D;
    private static final Double UPDATED_CAMARA = 2D;

    private static final Double DEFAULT_FRONT_CAMARA = 1D;
    private static final Double UPDATED_FRONT_CAMARA = 2D;

    private static final Integer DEFAULT_BATERIA = 1;
    private static final Integer UPDATED_BATERIA = 2;

    private static final Double DEFAULT_PULGADAS_PANTALLA = 1D;
    private static final Double UPDATED_PULGADAS_PANTALLA = 2D;

    private static final EnumResolucionPantalla DEFAULT_RESOLUCION_PANTALLA = EnumResolucionPantalla.CuatroK;
    private static final EnumResolucionPantalla UPDATED_RESOLUCION_PANTALLA = EnumResolucionPantalla.Mil_440p;

    private static final Integer DEFAULT_RAM = 1;
    private static final Integer UPDATED_RAM = 2;

    private static final EnumOS DEFAULT_SO = EnumOS.Android;
    private static final EnumOS UPDATED_SO = EnumOS.iOS;

    private static final Integer DEFAULT_ROM = 1;
    private static final Integer UPDATED_ROM = 2;

    @Inject
    private SmartphoneRepository smartphoneRepository;

    @Inject
    private SmartphoneService smartphoneService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSmartphoneMockMvc;

    private Smartphone smartphone;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SmartphoneResource smartphoneResource = new SmartphoneResource();
        ReflectionTestUtils.setField(smartphoneResource, "smartphoneService", smartphoneService);
        this.restSmartphoneMockMvc = MockMvcBuilders.standaloneSetup(smartphoneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Smartphone createEntity(EntityManager em) {
        Smartphone smartphone = new Smartphone()
                .modelo(DEFAULT_MODELO)
                .marca(DEFAULT_MARCA)
                .camara(DEFAULT_CAMARA)
                .frontCamara(DEFAULT_FRONT_CAMARA)
                .bateria(DEFAULT_BATERIA)
                .pulgadasPantalla(DEFAULT_PULGADAS_PANTALLA)
                .resolucionPantalla(DEFAULT_RESOLUCION_PANTALLA)
                .ram(DEFAULT_RAM)
                .so(DEFAULT_SO)
                .rom(DEFAULT_ROM);
        return smartphone;
    }

    @Before
    public void initTest() {
        smartphone = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmartphone() throws Exception {
        int databaseSizeBeforeCreate = smartphoneRepository.findAll().size();

        // Create the Smartphone

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isCreated());

        // Validate the Smartphone in the database
        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeCreate + 1);
        Smartphone testSmartphone = smartphoneList.get(smartphoneList.size() - 1);
        assertThat(testSmartphone.getModelo()).isEqualTo(DEFAULT_MODELO);
        assertThat(testSmartphone.getMarca()).isEqualTo(DEFAULT_MARCA);
        assertThat(testSmartphone.getCamara()).isEqualTo(DEFAULT_CAMARA);
        assertThat(testSmartphone.getFrontCamara()).isEqualTo(DEFAULT_FRONT_CAMARA);
        assertThat(testSmartphone.getBateria()).isEqualTo(DEFAULT_BATERIA);
        assertThat(testSmartphone.getPulgadasPantalla()).isEqualTo(DEFAULT_PULGADAS_PANTALLA);
        assertThat(testSmartphone.getResolucionPantalla()).isEqualTo(DEFAULT_RESOLUCION_PANTALLA);
        assertThat(testSmartphone.getRam()).isEqualTo(DEFAULT_RAM);
        assertThat(testSmartphone.getSo()).isEqualTo(DEFAULT_SO);
        assertThat(testSmartphone.getRom()).isEqualTo(DEFAULT_ROM);
    }

    @Test
    @Transactional
    public void createSmartphoneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smartphoneRepository.findAll().size();

        // Create the Smartphone with an existing ID
        Smartphone existingSmartphone = new Smartphone();
        existingSmartphone.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSmartphone)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkModeloIsRequired() throws Exception {
        int databaseSizeBeforeTest = smartphoneRepository.findAll().size();
        // set the field null
        smartphone.setModelo(null);

        // Create the Smartphone, which fails.

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isBadRequest());

        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMarcaIsRequired() throws Exception {
        int databaseSizeBeforeTest = smartphoneRepository.findAll().size();
        // set the field null
        smartphone.setMarca(null);

        // Create the Smartphone, which fails.

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isBadRequest());

        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCamaraIsRequired() throws Exception {
        int databaseSizeBeforeTest = smartphoneRepository.findAll().size();
        // set the field null
        smartphone.setCamara(null);

        // Create the Smartphone, which fails.

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isBadRequest());

        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFrontCamaraIsRequired() throws Exception {
        int databaseSizeBeforeTest = smartphoneRepository.findAll().size();
        // set the field null
        smartphone.setFrontCamara(null);

        // Create the Smartphone, which fails.

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isBadRequest());

        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBateriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = smartphoneRepository.findAll().size();
        // set the field null
        smartphone.setBateria(null);

        // Create the Smartphone, which fails.

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isBadRequest());

        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPulgadasPantallaIsRequired() throws Exception {
        int databaseSizeBeforeTest = smartphoneRepository.findAll().size();
        // set the field null
        smartphone.setPulgadasPantalla(null);

        // Create the Smartphone, which fails.

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isBadRequest());

        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResolucionPantallaIsRequired() throws Exception {
        int databaseSizeBeforeTest = smartphoneRepository.findAll().size();
        // set the field null
        smartphone.setResolucionPantalla(null);

        // Create the Smartphone, which fails.

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isBadRequest());

        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRamIsRequired() throws Exception {
        int databaseSizeBeforeTest = smartphoneRepository.findAll().size();
        // set the field null
        smartphone.setRam(null);

        // Create the Smartphone, which fails.

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isBadRequest());

        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSoIsRequired() throws Exception {
        int databaseSizeBeforeTest = smartphoneRepository.findAll().size();
        // set the field null
        smartphone.setSo(null);

        // Create the Smartphone, which fails.

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isBadRequest());

        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRomIsRequired() throws Exception {
        int databaseSizeBeforeTest = smartphoneRepository.findAll().size();
        // set the field null
        smartphone.setRom(null);

        // Create the Smartphone, which fails.

        restSmartphoneMockMvc.perform(post("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isBadRequest());

        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSmartphones() throws Exception {
        // Initialize the database
        smartphoneRepository.saveAndFlush(smartphone);

        // Get all the smartphoneList
        restSmartphoneMockMvc.perform(get("/api/smartphones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smartphone.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO.toString())))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA.toString())))
            .andExpect(jsonPath("$.[*].camara").value(hasItem(DEFAULT_CAMARA.doubleValue())))
            .andExpect(jsonPath("$.[*].frontCamara").value(hasItem(DEFAULT_FRONT_CAMARA.doubleValue())))
            .andExpect(jsonPath("$.[*].bateria").value(hasItem(DEFAULT_BATERIA)))
            .andExpect(jsonPath("$.[*].pulgadasPantalla").value(hasItem(DEFAULT_PULGADAS_PANTALLA.doubleValue())))
            .andExpect(jsonPath("$.[*].resolucionPantalla").value(hasItem(DEFAULT_RESOLUCION_PANTALLA.toString())))
            .andExpect(jsonPath("$.[*].ram").value(hasItem(DEFAULT_RAM)))
            .andExpect(jsonPath("$.[*].so").value(hasItem(DEFAULT_SO.toString())))
            .andExpect(jsonPath("$.[*].rom").value(hasItem(DEFAULT_ROM)));
    }

    @Test
    @Transactional
    public void getSmartphone() throws Exception {
        // Initialize the database
        smartphoneRepository.saveAndFlush(smartphone);

        // Get the smartphone
        restSmartphoneMockMvc.perform(get("/api/smartphones/{id}", smartphone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smartphone.getId().intValue()))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO.toString()))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA.toString()))
            .andExpect(jsonPath("$.camara").value(DEFAULT_CAMARA.doubleValue()))
            .andExpect(jsonPath("$.frontCamara").value(DEFAULT_FRONT_CAMARA.doubleValue()))
            .andExpect(jsonPath("$.bateria").value(DEFAULT_BATERIA))
            .andExpect(jsonPath("$.pulgadasPantalla").value(DEFAULT_PULGADAS_PANTALLA.doubleValue()))
            .andExpect(jsonPath("$.resolucionPantalla").value(DEFAULT_RESOLUCION_PANTALLA.toString()))
            .andExpect(jsonPath("$.ram").value(DEFAULT_RAM))
            .andExpect(jsonPath("$.so").value(DEFAULT_SO.toString()))
            .andExpect(jsonPath("$.rom").value(DEFAULT_ROM));
    }

    @Test
    @Transactional
    public void getNonExistingSmartphone() throws Exception {
        // Get the smartphone
        restSmartphoneMockMvc.perform(get("/api/smartphones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmartphone() throws Exception {
        // Initialize the database
        smartphoneService.save(smartphone);

        int databaseSizeBeforeUpdate = smartphoneRepository.findAll().size();

        // Update the smartphone
        Smartphone updatedSmartphone = smartphoneRepository.findOne(smartphone.getId());
        updatedSmartphone
                .modelo(UPDATED_MODELO)
                .marca(UPDATED_MARCA)
                .camara(UPDATED_CAMARA)
                .frontCamara(UPDATED_FRONT_CAMARA)
                .bateria(UPDATED_BATERIA)
                .pulgadasPantalla(UPDATED_PULGADAS_PANTALLA)
                .resolucionPantalla(UPDATED_RESOLUCION_PANTALLA)
                .ram(UPDATED_RAM)
                .so(UPDATED_SO)
                .rom(UPDATED_ROM);

        restSmartphoneMockMvc.perform(put("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSmartphone)))
            .andExpect(status().isOk());

        // Validate the Smartphone in the database
        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeUpdate);
        Smartphone testSmartphone = smartphoneList.get(smartphoneList.size() - 1);
        assertThat(testSmartphone.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testSmartphone.getMarca()).isEqualTo(UPDATED_MARCA);
        assertThat(testSmartphone.getCamara()).isEqualTo(UPDATED_CAMARA);
        assertThat(testSmartphone.getFrontCamara()).isEqualTo(UPDATED_FRONT_CAMARA);
        assertThat(testSmartphone.getBateria()).isEqualTo(UPDATED_BATERIA);
        assertThat(testSmartphone.getPulgadasPantalla()).isEqualTo(UPDATED_PULGADAS_PANTALLA);
        assertThat(testSmartphone.getResolucionPantalla()).isEqualTo(UPDATED_RESOLUCION_PANTALLA);
        assertThat(testSmartphone.getRam()).isEqualTo(UPDATED_RAM);
        assertThat(testSmartphone.getSo()).isEqualTo(UPDATED_SO);
        assertThat(testSmartphone.getRom()).isEqualTo(UPDATED_ROM);
    }

    @Test
    @Transactional
    public void updateNonExistingSmartphone() throws Exception {
        int databaseSizeBeforeUpdate = smartphoneRepository.findAll().size();

        // Create the Smartphone

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSmartphoneMockMvc.perform(put("/api/smartphones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smartphone)))
            .andExpect(status().isCreated());

        // Validate the Smartphone in the database
        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSmartphone() throws Exception {
        // Initialize the database
        smartphoneService.save(smartphone);

        int databaseSizeBeforeDelete = smartphoneRepository.findAll().size();

        // Get the smartphone
        restSmartphoneMockMvc.perform(delete("/api/smartphones/{id}", smartphone.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Smartphone> smartphoneList = smartphoneRepository.findAll();
        assertThat(smartphoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
