import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedBlobField, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getIdentityDocumentReviews } from 'app/entities/identity-document-review/identity-document-review.reducer';
import { DocumentStatus } from 'app/shared/model/enumerations/document-status.model';
import { DocumentType } from 'app/shared/model/enumerations/document-type.model';
import { createEntity, getEntity, reset, updateEntity } from './identity-document.reducer';

export const IdentityDocumentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const identityDocumentReviews = useAppSelector(state => state.identityDocumentReview.entities);
  const identityDocumentEntity = useAppSelector(state => state.identityDocument.entity);
  const loading = useAppSelector(state => state.identityDocument.loading);
  const updating = useAppSelector(state => state.identityDocument.updating);
  const updateSuccess = useAppSelector(state => state.identityDocument.updateSuccess);
  const documentStatusValues = Object.keys(DocumentStatus);
  const documentTypeValues = Object.keys(DocumentType);

  const handleClose = () => {
    navigate(`/identity-document${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getIdentityDocumentReviews({}));
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

    const entity = {
      ...identityDocumentEntity,
      ...values,
      review: identityDocumentReviews.find(it => it.id.toString() === values.review?.toString()),
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
          documentStatus: 'PENDING',
          documentType: 'ID_VERIFICATION',
          ...identityDocumentEntity,
          createdDate: convertDateTimeFromServer(identityDocumentEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(identityDocumentEntity.lastModifiedDate),
          review: identityDocumentEntity?.review?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.identityDocument.home.createOrEditLabel" data-cy="IdentityDocumentCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.identityDocument.home.createOrEditLabel">
              Create or edit a IdentityDocument
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
                  id="identity-document-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocument.documentName')}
                id="identity-document-documentName"
                name="documentName"
                data-cy="documentName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocument.documentDescription')}
                id="identity-document-documentDescription"
                name="documentDescription"
                data-cy="documentDescription"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocument.documentStatus')}
                id="identity-document-documentStatus"
                name="documentStatus"
                data-cy="documentStatus"
                type="select"
              >
                {documentStatusValues.map(documentStatus => (
                  <option value={documentStatus} key={documentStatus}>
                    {translate(`monsterdamChicksApp.DocumentStatus.${documentStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocument.documentType')}
                id="identity-document-documentType"
                name="documentType"
                data-cy="documentType"
                type="select"
              >
                {documentTypeValues.map(documentType => (
                  <option value={documentType} key={documentType}>
                    {translate(`monsterdamChicksApp.DocumentType.${documentType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedBlobField
                label={translate('monsterdamChicksApp.identityDocument.fileDocument')}
                id="identity-document-fileDocument"
                name="fileDocument"
                data-cy="fileDocument"
                openActionLabel={translate('entity.action.open')}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocument.fileDocumentS3Key')}
                id="identity-document-fileDocumentS3Key"
                name="fileDocumentS3Key"
                data-cy="fileDocumentS3Key"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocument.createdDate')}
                id="identity-document-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocument.lastModifiedDate')}
                id="identity-document-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocument.createdBy')}
                id="identity-document-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.identityDocument.lastModifiedBy')}
                id="identity-document-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                id="identity-document-review"
                name="review"
                data-cy="review"
                label={translate('monsterdamChicksApp.identityDocument.review')}
                type="select"
              >
                <option value="" key="0" />
                {identityDocumentReviews
                  ? identityDocumentReviews.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/identity-document" replace color="info">
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

export default IdentityDocumentUpdate;
