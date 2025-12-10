import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { AdminGender } from 'app/shared/model/enumerations/admin-gender.model';
import { createEntity, getEntity, reset, updateEntity } from './platform-admin-user.reducer';

export const PlatformAdminUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const platformAdminUserEntity = useAppSelector(state => state.platformAdminUser.entity);
  const loading = useAppSelector(state => state.platformAdminUser.loading);
  const updating = useAppSelector(state => state.platformAdminUser.updating);
  const updateSuccess = useAppSelector(state => state.platformAdminUser.updateSuccess);
  const adminGenderValues = Object.keys(AdminGender);

  const handleClose = () => {
    navigate(`/platform-admin-user${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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

    const entity = {
      ...platformAdminUserEntity,
      ...values,
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
        }
      : {
          gender: 'MALE',
          ...platformAdminUserEntity,
          lastLoginDate: convertDateTimeFromServer(platformAdminUserEntity.lastLoginDate),
          createdDate: convertDateTimeFromServer(platformAdminUserEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(platformAdminUserEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.platformAdminUser.home.createOrEditLabel" data-cy="PlatformAdminUserCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.platformAdminUser.home.createOrEditLabel">
              Create or edit a PlatformAdminUser
            </Translate>
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
                  id="platform-admin-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.fullName')}
                id="platform-admin-user-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.emailAddress')}
                id="platform-admin-user-emailAddress"
                name="emailAddress"
                data-cy="emailAddress"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.nickName')}
                id="platform-admin-user-nickName"
                name="nickName"
                data-cy="nickName"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.gender')}
                id="platform-admin-user-gender"
                name="gender"
                data-cy="gender"
                type="select"
              >
                {adminGenderValues.map(adminGender => (
                  <option value={adminGender} key={adminGender}>
                    {translate(`monsterdamChicksApp.AdminGender.${adminGender}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.mobilePhone')}
                id="platform-admin-user-mobilePhone"
                name="mobilePhone"
                data-cy="mobilePhone"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.lastLoginDate')}
                id="platform-admin-user-lastLoginDate"
                name="lastLoginDate"
                data-cy="lastLoginDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.birthDate')}
                id="platform-admin-user-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.createdDate')}
                id="platform-admin-user-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.lastModifiedDate')}
                id="platform-admin-user-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.createdBy')}
                id="platform-admin-user-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.lastModifiedBy')}
                id="platform-admin-user-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.platformAdminUser.isDeleted')}
                id="platform-admin-user-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/platform-admin-user" replace color="info">
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

export default PlatformAdminUserUpdate;
