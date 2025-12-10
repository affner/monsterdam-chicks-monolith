import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { AssociationStatus } from 'app/shared/model/enumerations/association-status.model';
import { createEntity, getEntity, reset, updateEntity } from './user-association.reducer';

export const UserAssociationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userAssociationEntity = useAppSelector(state => state.userAssociation.entity);
  const loading = useAppSelector(state => state.userAssociation.loading);
  const updating = useAppSelector(state => state.userAssociation.updating);
  const updateSuccess = useAppSelector(state => state.userAssociation.updateSuccess);
  const associationStatusValues = Object.keys(AssociationStatus);

  const handleClose = () => {
    navigate(`/user-association${location.search}`);
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
    values.requestedDate = convertDateTimeToServer(values.requestedDate);
    values.expiryDate = convertDateTimeToServer(values.expiryDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.ownerId !== undefined && typeof values.ownerId !== 'number') {
      values.ownerId = Number(values.ownerId);
    }

    const entity = {
      ...userAssociationEntity,
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
          requestedDate: displayDefaultDateTime(),
          expiryDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          status: 'REQUESTED',
          ...userAssociationEntity,
          requestedDate: convertDateTimeFromServer(userAssociationEntity.requestedDate),
          expiryDate: convertDateTimeFromServer(userAssociationEntity.expiryDate),
          createdDate: convertDateTimeFromServer(userAssociationEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(userAssociationEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.userAssociation.home.createOrEditLabel" data-cy="UserAssociationCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.userAssociation.home.createOrEditLabel">Create or edit a UserAssociation</Translate>
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
                  id="user-association-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.userAssociation.requestedDate')}
                id="user-association-requestedDate"
                name="requestedDate"
                data-cy="requestedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userAssociation.status')}
                id="user-association-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {associationStatusValues.map(associationStatus => (
                  <option value={associationStatus} key={associationStatus}>
                    {translate(`monsterdamChicksApp.AssociationStatus.${associationStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.userAssociation.associationToken')}
                id="user-association-associationToken"
                name="associationToken"
                data-cy="associationToken"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userAssociation.expiryDate')}
                id="user-association-expiryDate"
                name="expiryDate"
                data-cy="expiryDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userAssociation.createdDate')}
                id="user-association-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userAssociation.lastModifiedDate')}
                id="user-association-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userAssociation.createdBy')}
                id="user-association-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userAssociation.lastModifiedBy')}
                id="user-association-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userAssociation.isDeleted')}
                id="user-association-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.userAssociation.ownerId')}
                id="user-association-ownerId"
                name="ownerId"
                data-cy="ownerId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-association" replace color="info">
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

export default UserAssociationUpdate;
