import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedBlobField, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { getEntities as getUserSettings } from 'app/entities/user-settings/user-settings.reducer';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { getEntities as getChatRooms } from 'app/entities/chat-room/chat-room.reducer';
import { UserGender } from 'app/shared/model/enumerations/user-gender.model';
import { createEntity, getEntity, reset, updateEntity } from './user-lite.reducer';

export const UserLiteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const userSettings = useAppSelector(state => state.userSettings.entities);
  const countries = useAppSelector(state => state.country.entities);
  const chatRooms = useAppSelector(state => state.chatRoom.entities);
  const userLiteEntity = useAppSelector(state => state.userLite.entity);
  const loading = useAppSelector(state => state.userLite.loading);
  const updating = useAppSelector(state => state.userLite.updating);
  const updateSuccess = useAppSelector(state => state.userLite.updateSuccess);
  const userGenderValues = Object.keys(UserGender);

  const handleClose = () => {
    navigate(`/user-lite${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getUserSettings({}));
    dispatch(getCountries({}));
    dispatch(getChatRooms({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...userLiteEntity,
      ...values,
      profile: userProfiles.find(it => it.id.toString() === values.profile?.toString()),
      settings: userSettings.find(it => it.id.toString() === values.settings?.toString()),
      countryOfBirth: countries.find(it => it.id.toString() === values.countryOfBirth?.toString()),
      chatRooms: mapIdList(values.chatRooms),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          gender: 'MALE',
          ...userLiteEntity,
          createdDate: convertDateTimeFromServer(userLiteEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userLiteEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(userLiteEntity.deletedDate),
          profile: userLiteEntity?.profile?.id,
          settings: userLiteEntity?.settings?.id,
          countryOfBirth: userLiteEntity?.countryOfBirth?.id,
          chatRooms: userLiteEntity?.chatRooms?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.userLite.home.createOrEditLabel" data-cy="UserLiteCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.userLite.home.createOrEditLabel">Create or edit a UserLite</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="user-lite-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.thumbnailS3Key')}
                id="user-lite-thumbnailS3Key"
                name="thumbnailS3Key"
                data-cy="thumbnailS3Key"
                type="text"
              />
              <ValidatedBlobField
                label={translate('monsterdamChicksApp.userLite.thumbnail')}
                id="user-lite-thumbnail"
                name="thumbnail"
                data-cy="thumbnail"
                isImage
                accept="image/*"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.birthDate')}
                id="user-lite-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.gender')}
                id="user-lite-gender"
                name="gender"
                data-cy="gender"
                type="select"
              >
                {userGenderValues.map(userGender => (
                  <option value={userGender} key={userGender}>
                    {translate(`monsterdamChicksApp.UserGender.${userGender}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.createdDate')}
                id="user-lite-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.lastModifiedDate')}
                id="user-lite-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.createdBy')}
                id="user-lite-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.lastModifiedBy')}
                id="user-lite-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.deletedDate')}
                id="user-lite-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.nickName')}
                id="user-lite-nickName"
                name="nickName"
                data-cy="nickName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: {
                    value: /^[a-z0-9_-]{3,16}$/,
                    message: translate('entity.validation.pattern', { pattern: '^[a-z0-9_-]{3,16}$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.fullName')}
                id="user-lite-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: { value: /^[a-zA-Z0-9 ]*$/, message: translate('entity.validation.pattern', { pattern: '^[a-zA-Z0-9 ]*$' }) },
                }}
              />
              <ValidatedField
                id="user-lite-profile"
                name="profile"
                data-cy="profile"
                label={translate('monsterdamChicksApp.userLite.profile')}
                type="select"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-lite-settings"
                name="settings"
                data-cy="settings"
                label={translate('monsterdamChicksApp.userLite.settings')}
                type="select"
              >
                <option value="" key="0" />
                {userSettings
                  ? userSettings.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-lite-countryOfBirth"
                name="countryOfBirth"
                data-cy="countryOfBirth"
                label={translate('monsterdamChicksApp.userLite.countryOfBirth')}
                type="select"
              >
                <option value="" key="0" />
                {countries
                  ? countries.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.userLite.chatRooms')}
                id="user-lite-chatRooms"
                data-cy="chatRooms"
                type="select"
                multiple
                name="chatRooms"
              >
                <option value="" key="0" />
                {chatRooms
                  ? chatRooms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-lite" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UserLiteUpdate;
