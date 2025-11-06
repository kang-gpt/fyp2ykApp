package com.yk.booking.web.rest;

import static com.yk.booking.domain.SportAsserts.*;
import static com.yk.booking.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yk.booking.IntegrationTest;
import com.yk.booking.domain.Sport;
import com.yk.booking.repository.SportRepository;
import com.yk.booking.service.dto.SportDTO;
import com.yk.booking.service.mapper.SportMapper;
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
 * Integration tests for the {@link SportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SportResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private SportMapper sportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSportMockMvc;

    private Sport sport;

    private Sport insertedSport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sport createEntity() {
        return new Sport().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sport createUpdatedEntity() {
        return new Sport().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        sport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSport != null) {
            sportRepository.delete(insertedSport);
            insertedSport = null;
        }
    }

    @Test
    @Transactional
    void createSport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);
        var returnedSportDTO = om.readValue(
            restSportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SportDTO.class
        );

        // Validate the Sport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSport = sportMapper.toEntity(returnedSportDTO);
        assertSportUpdatableFieldsEquals(returnedSport, getPersistedSport(returnedSport));

        insertedSport = returnedSport;
    }

    @Test
    @Transactional
    void createSportWithExistingId() throws Exception {
        // Create the Sport with an existing ID
        sport.setId(1L);
        SportDTO sportDTO = sportMapper.toDto(sport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sport.setName(null);

        // Create the Sport, which fails.
        SportDTO sportDTO = sportMapper.toDto(sport);

        restSportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSports() throws Exception {
        // Initialize the database
        insertedSport = sportRepository.saveAndFlush(sport);

        // Get all the sportList
        restSportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sport.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSport() throws Exception {
        // Initialize the database
        insertedSport = sportRepository.saveAndFlush(sport);

        // Get the sport
        restSportMockMvc
            .perform(get(ENTITY_API_URL_ID, sport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSport() throws Exception {
        // Get the sport
        restSportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSport() throws Exception {
        // Initialize the database
        insertedSport = sportRepository.saveAndFlush(sport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sport
        Sport updatedSport = sportRepository.findById(sport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSport are not directly saved in db
        em.detach(updatedSport);
        updatedSport.name(UPDATED_NAME);
        SportDTO sportDTO = sportMapper.toDto(updatedSport);

        restSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sportDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSportToMatchAllProperties(updatedSport);
    }

    @Test
    @Transactional
    void putNonExistingSport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sport.setId(longCount.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sportDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sport.setId(longCount.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sport.setId(longCount.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSportWithPatch() throws Exception {
        // Initialize the database
        insertedSport = sportRepository.saveAndFlush(sport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sport using partial update
        Sport partialUpdatedSport = new Sport();
        partialUpdatedSport.setId(sport.getId());

        restSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSport))
            )
            .andExpect(status().isOk());

        // Validate the Sport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSportUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSport, sport), getPersistedSport(sport));
    }

    @Test
    @Transactional
    void fullUpdateSportWithPatch() throws Exception {
        // Initialize the database
        insertedSport = sportRepository.saveAndFlush(sport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sport using partial update
        Sport partialUpdatedSport = new Sport();
        partialUpdatedSport.setId(sport.getId());

        partialUpdatedSport.name(UPDATED_NAME);

        restSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSport))
            )
            .andExpect(status().isOk());

        // Validate the Sport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSportUpdatableFieldsEquals(partialUpdatedSport, getPersistedSport(partialUpdatedSport));
    }

    @Test
    @Transactional
    void patchNonExistingSport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sport.setId(longCount.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sport.setId(longCount.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sport.setId(longCount.incrementAndGet());

        // Create the Sport
        SportDTO sportDTO = sportMapper.toDto(sport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSport() throws Exception {
        // Initialize the database
        insertedSport = sportRepository.saveAndFlush(sport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sport
        restSportMockMvc
            .perform(delete(ENTITY_API_URL_ID, sport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sportRepository.count();
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

    protected Sport getPersistedSport(Sport sport) {
        return sportRepository.findById(sport.getId()).orElseThrow();
    }

    protected void assertPersistedSportToMatchAllProperties(Sport expectedSport) {
        assertSportAllPropertiesEquals(expectedSport, getPersistedSport(expectedSport));
    }

    protected void assertPersistedSportToMatchUpdatableProperties(Sport expectedSport) {
        assertSportAllUpdatablePropertiesEquals(expectedSport, getPersistedSport(expectedSport));
    }
}
