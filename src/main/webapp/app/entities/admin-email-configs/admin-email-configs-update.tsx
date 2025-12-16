import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { EmailTemplateType } from 'app/shared/model/enumerations/email-template-type.model';
import { createEntity, getEntity, reset, updateEntity } from './admin-email-configs.reducer';

export const AdminEmailConfigsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const adminEmailConfigsEntity = useAppSelector(state => state.adminEmailConfigs.entity);
  const loading = useAppSelector(state => state.adminEmailConfigs.loading);
  const updating = useAppSelector(state => state.adminEmailConfigs.updating);
  const updateSuccess = useAppSelector(state => state.adminEmailConfigs.updateSuccess);
  const emailTemplateTypeValues = Object.keys(EmailTemplateType);

  const handleClose = () => {
    navigate(`/admin-email-configs${location.search}`);
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...adminEmailConfigsEntity,
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
          deletedDate: displayDefaultDateTime(),
        }
      : {
          mailTemplateType: 'NOTIFICATION',
          ...adminEmailConfigsEntity,
          createdDate: convertDateTimeFromServer(adminEmailConfigsEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(adminEmailConfigsEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(adminEmailConfigsEntity.deletedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.adminEmailConfigs.home.createOrEditLabel" data-cy="AdminEmailConfigsCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.adminEmailConfigs.home.createOrEditLabel">
              Create or edit a AdminEmailConfigs
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
                  id="admin-email-configs-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.adminEmailConfigs.title')}
                id="admin-email-configs-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminEmailConfigs.subject')}
                id="admin-email-configs-subject"
                name="subject"
                data-cy="subject"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminEmailConfigs.content')}
                id="admin-email-configs-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminEmailConfigs.mailTemplateType')}
                id="admin-email-configs-mailTemplateType"
                name="mailTemplateType"
                data-cy="mailTemplateType"
                type="select"
              >
                {emailTemplateTypeValues.map(emailTemplateType => (
                  <option value={emailTemplateType} key={emailTemplateType}>
                    {translate(`monsterdamChicksApp.EmailTemplateType.${emailTemplateType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.adminEmailConfigs.createdDate')}
                id="admin-email-configs-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminEmailConfigs.lastModifiedDate')}
                id="admin-email-configs-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminEmailConfigs.createdBy')}
                id="admin-email-configs-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminEmailConfigs.lastModifiedBy')}
                id="admin-email-configs-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.adminEmailConfigs.deletedDate')}
                id="admin-email-configs-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/admin-email-configs" replace color="info">
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

export default AdminEmailConfigsUpdate;
