import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getStates } from 'app/entities/state/state.reducer';
import { createEntity, getEntity, reset, updateEntity } from './user-profile.reducer';

export const UserProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const states = useAppSelector(state => state.state.entities);
  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  const loading = useAppSelector(state => state.userProfile.loading);
  const updating = useAppSelector(state => state.userProfile.updating);
  const updateSuccess = useAppSelector(state => state.userProfile.updateSuccess);

  const handleClose = () => {
    navigate(`/user-profile${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStates({}));
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
    values.lastLoginDate = convertDateTimeToServer(values.lastLoginDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...userProfileEntity,
      ...values,
      stateOfResidence: states.find(it => it.id.toString() === values.stateOfResidence?.toString()),
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
          lastLoginDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          ...userProfileEntity,
          lastLoginDate: convertDateTimeFromServer(userProfileEntity.lastLoginDate),
          createdDate: convertDateTimeFromServer(userProfileEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userProfileEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(userProfileEntity.deletedDate),
          stateOfResidence: userProfileEntity?.stateOfResidence?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.userProfile.home.createOrEditLabel" data-cy="UserProfileCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.userProfile.home.createOrEditLabel">Create or edit a UserProfile</Translate>
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
                  id="user-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.emailContact')}
                id="user-profile-emailContact"
                name="emailContact"
                data-cy="emailContact"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.profilePhotoS3Key')}
                id="user-profile-profilePhotoS3Key"
                name="profilePhotoS3Key"
                data-cy="profilePhotoS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.coverPhotoS3Key')}
                id="user-profile-coverPhotoS3Key"
                name="coverPhotoS3Key"
                data-cy="coverPhotoS3Key"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.mainContentUrl')}
                id="user-profile-mainContentUrl"
                name="mainContentUrl"
                data-cy="mainContentUrl"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.mobilePhone')}
                id="user-profile-mobilePhone"
                name="mobilePhone"
                data-cy="mobilePhone"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.websiteUrl')}
                id="user-profile-websiteUrl"
                name="websiteUrl"
                data-cy="websiteUrl"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.amazonWishlistUrl')}
                id="user-profile-amazonWishlistUrl"
                name="amazonWishlistUrl"
                data-cy="amazonWishlistUrl"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.lastLoginDate')}
                id="user-profile-lastLoginDate"
                name="lastLoginDate"
                data-cy="lastLoginDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.biography')}
                id="user-profile-biography"
                name="biography"
                data-cy="biography"
                type="textarea"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.isFree')}
                id="user-profile-isFree"
                name="isFree"
                data-cy="isFree"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.createdDate')}
                id="user-profile-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.lastModifiedDate')}
                id="user-profile-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.createdBy')}
                id="user-profile-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.lastModifiedBy')}
                id="user-profile-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userProfile.deletedDate')}
                id="user-profile-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="user-profile-stateOfResidence"
                name="stateOfResidence"
                data-cy="stateOfResidence"
                label={translate('monsterdamChicksApp.userProfile.stateOfResidence')}
                type="select"
              >
                <option value="" key="0" />
                {states
                  ? states.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-profile" replace color="info">
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

export default UserProfileUpdate;
