package com.yk.booking.web.rest;

import static com.yk.booking.domain.CourtAsserts.*;
import static com.yk.booking.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yk.booking.IntegrationTest;
import com.yk.booking.domain.Court;
import com.yk.booking.repository.CourtRepository;
import com.yk.booking.service.dto.CourtDTO;
import com.yk.booking.service.mapper.CourtMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CourtResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourtResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/courts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private CourtMapper courtMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourtMockMvc;

    private Court court;

    private Court insertedCourt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Court createEntity() {
        return new Court().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Court createUpdatedEntity() {
        return new Court().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        court = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCourt != null) {
            courtRepository.delete(insertedCourt);
            insertedCourt = null;
        }
    }

    @Test
    @Transactional
    void createCourt() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Court
        CourtDTO courtDTO = courtMapper.toDto(court);
        var returnedCourtDTO = om.readValue(
            restCourtMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CourtDTO.class
        );

        // Validate the Court in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCourt = courtMapper.toEntity(returnedCourtDTO);
        assertCourtUpdatableFieldsEquals(returnedCourt, getPersistedCourt(returnedCourt));

        insertedCourt = returnedCourt;
    }

    @Test
    @Transactional
    void createCourtWithExistingId() throws Exception {
        // Create the Court with an existing ID
        court.setId(1L);
        CourtDTO courtDTO = courtMapper.toDto(court);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Court in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        court.setName(null);

        // Create the Court, which fails.
        CourtDTO courtDTO = courtMapper.toDto(court);

        restCourtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourts() throws Exception {
        // Initialize the database
        insertedCourt = courtRepository.saveAndFlush(court);

        // Get all the courtList
        restCourtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(court.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCourt() throws Exception {
        // Initialize the database
        insertedCourt = courtRepository.saveAndFlush(court);

        // Get the court
        restCourtMockMvc
            .perform(get(ENTITY_API_URL_ID, court.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(court.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCourt() throws Exception {
        // Get the court
        restCourtMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCourt() throws Exception {
        // Initialize the database
        insertedCourt = courtRepository.saveAndFlush(court);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the court
        Court updatedCourt = courtRepository.findById(court.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCourt are not directly saved in db
        em.detach(updatedCourt);
        updatedCourt.name(UPDATED_NAME);
        CourtDTO courtDTO = courtMapper.toDto(updatedCourt);

        restCourtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courtDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtDTO))
            )
            .andExpect(status().isOk());

        // Validate the Court in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCourtToMatchAllProperties(updatedCourt);
    }

    @Test
    @Transactional
    void putNonExistingCourt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        court.setId(longCount.incrementAndGet());

        // Create the Court
        CourtDTO courtDTO = courtMapper.toDto(court);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courtDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Court in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        court.setId(longCount.incrementAndGet());

        // Create the Court
        CourtDTO courtDTO = courtMapper.toDto(court);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Court in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        court.setId(longCount.incrementAndGet());

        // Create the Court
        CourtDTO courtDTO = courtMapper.toDto(court);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courtDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Court in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourtWithPatch() throws Exception {
        // Initialize the database
        insertedCourt = courtRepository.saveAndFlush(court);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the court using partial update
        Court partialUpdatedCourt = new Court();
        partialUpdatedCourt.setId(court.getId());

        partialUpdatedCourt.name(UPDATED_NAME);

        restCourtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCourt))
            )
            .andExpect(status().isOk());

        // Validate the Court in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourtUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCourt, court), getPersistedCourt(court));
    }

    @Test
    @Transactional
    void fullUpdateCourtWithPatch() throws Exception {
        // Initialize the database
        insertedCourt = courtRepository.saveAndFlush(court);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the court using partial update
        Court partialUpdatedCourt = new Court();
        partialUpdatedCourt.setId(court.getId());

        partialUpdatedCourt.name(UPDATED_NAME);

        restCourtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourt.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCourt))
            )
            .andExpect(status().isOk());

        // Validate the Court in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourtUpdatableFieldsEquals(partialUpdatedCourt, getPersistedCourt(partialUpdatedCourt));
    }

    @Test
    @Transactional
    void patchNonExistingCourt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        court.setId(longCount.incrementAndGet());

        // Create the Court
        CourtDTO courtDTO = courtMapper.toDto(court);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courtDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(courtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Court in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        court.setId(longCount.incrementAndGet());

        // Create the Court
        CourtDTO courtDTO = courtMapper.toDto(court);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(courtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Court in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourt() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        court.setId(longCount.incrementAndGet());

        // Create the Court
        CourtDTO courtDTO = courtMapper.toDto(court);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourtMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(courtDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Court in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourt() throws Exception {
        // Initialize the database
        insertedCourt = courtRepository.saveAndFlush(court);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the court
        restCourtMockMvc
            .perform(delete(ENTITY_API_URL_ID, court.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return courtRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Court getPersistedCourt(Court court) {
        return courtRepository.findById(court.getId()).orElseThrow();
    }

    protected void assertPersistedCourtToMatchAllProperties(Court expectedCourt) {
        assertCourtAllPropertiesEquals(expectedCourt, getPersistedCourt(expectedCourt));
    }

    protected void assertPersistedCourtToMatchUpdatableProperties(Court expectedCourt) {
        assertCourtAllUpdatablePropertiesEquals(expectedCourt, getPersistedCourt(expectedCourt));
    }
}
