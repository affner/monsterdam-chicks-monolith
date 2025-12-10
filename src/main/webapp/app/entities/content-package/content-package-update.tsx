import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './content-package.reducer';

export const ContentPackageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const contentPackageEntity = useAppSelector(state => state.contentPackage.entity);
  const loading = useAppSelector(state => state.contentPackage.loading);
  const updating = useAppSelector(state => state.contentPackage.updating);
  const updateSuccess = useAppSelector(state => state.contentPackage.updateSuccess);

  const handleClose = () => {
    navigate(`/content-package${location.search}`);
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    if (values.videoCount !== undefined && typeof values.videoCount !== 'number') {
      values.videoCount = Number(values.videoCount);
    }
    if (values.imageCount !== undefined && typeof values.imageCount !== 'number') {
      values.imageCount = Number(values.imageCount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    if (values.messageId !== undefined && typeof values.messageId !== 'number') {
      values.messageId = Number(values.messageId);
    }
    if (values.postId !== undefined && typeof values.postId !== 'number') {
      values.postId = Number(values.postId);
    }

    const entity = {
      ...contentPackageEntity,
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...contentPackageEntity,
          createdDate: convertDateTimeFromServer(contentPackageEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(contentPackageEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.contentPackage.home.createOrEditLabel" data-cy="ContentPackageCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.contentPackage.home.createOrEditLabel">Create or edit a ContentPackage</Translate>
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
                  id="content-package-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.amount')}
                id="content-package-amount"
                name="amount"
                data-cy="amount"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.videoCount')}
                id="content-package-videoCount"
                name="videoCount"
                data-cy="videoCount"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.imageCount')}
                id="content-package-imageCount"
                name="imageCount"
                data-cy="imageCount"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.isPaidContent')}
                id="content-package-isPaidContent"
                name="isPaidContent"
                data-cy="isPaidContent"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.createdDate')}
                id="content-package-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.lastModifiedDate')}
                id="content-package-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.createdBy')}
                id="content-package-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.lastModifiedBy')}
                id="content-package-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.isDeleted')}
                id="content-package-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.messageId')}
                id="content-package-messageId"
                name="messageId"
                data-cy="messageId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.contentPackage.postId')}
                id="content-package-postId"
                name="postId"
                data-cy="postId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/content-package" replace color="info">
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

export default ContentPackageUpdate;
